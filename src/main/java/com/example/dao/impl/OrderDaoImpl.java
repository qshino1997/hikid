package com.example.dao.impl;

import com.example.dao.BaseDAO;
import com.example.dao.OrderDao;
import com.example.entity.Order;
import com.example.entity.OrderItem;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDaoImpl extends BaseDAO implements OrderDao {
    @Override
    public void save(Order order) {
        currentSession().saveOrUpdate(order);
    }

    @Override
    public Order findByPaymentId(String paymentId) {
        String hql = "FROM Order o WHERE o.payment_id = :pid";
        return currentSession()
                .createQuery(hql, Order.class)
                .setParameter("pid", paymentId)
                .uniqueResult();
    }

    @Override
    public List<Order> findByDateRange(LocalDate start, LocalDate end, String keyword, int offset, int limit) {
        // Chuyển sang DateTime để so sánh với LocalDateTime trong DB
        LocalDateTime startDt = (start != null)
                ? start.atStartOfDay()
                : null;
        LocalDateTime endDt = (end != null)
                ? end.atTime(23, 59, 59)
                : null;

        String hql = "FROM Order o WHERE (:startDt IS NULL OR o.created_at >= :startDt) " +
                "AND (:endDt IS NULL OR o.created_at <= :endDt)";

        boolean hasKeyword = StringUtils.hasText(keyword);
        Integer userId = null;

        if (keyword != null && !keyword.trim().isEmpty()) {
            if (hasKeyword) {
                try {
                    userId = Integer.valueOf(keyword.trim());
                } catch (NumberFormatException ignored) {
                }
            }

            if(userId != null){
                hql += " AND ((:keywordUser IS NOT NULL AND o.user_id = :keywordUser) "
                        + " OR o.payment_id = :keywordPayment)";
            } else {
                hql += " AND o.payment_id = :keywordPayment ";
            }
        }



        Query<Order> q = currentSession().createQuery(hql, Order.class);
        q.setParameter("startDt", startDt);
        q.setParameter("endDt", endDt);

        if (keyword != null && !keyword.trim().isEmpty()) {
            if (userId != null) {
                q.setParameter("keywordUser",userId);
            }
            q.setParameter("keywordPayment", keyword.trim());
        }

        q.setFirstResult((offset - 1) * limit);
        q.setMaxResults(limit);

        return q.getResultList();
    }

    @Override
    public Map<LocalDate, Long> countOrdersByDate(LocalDate startDate, LocalDate endDate) {
        // HQL: dùng function để cast timestamp về date
        String hql = "select function('date', o.created_at), count(o) "
                + "from Order o "
                + "where (:start is null or o.created_at >= :startAt) "
                + "  and (:end   is null or o.created_at <= :endAt) "
                + "group by function('date', o.created_at) "
                + "order by function('date', o.created_at)";
        Query<Object[]> q = currentSession().createQuery(hql, Object[].class);

        if (startDate != null) {
            q.setParameter("start", startDate);
            q.setParameter("startAt", startDate.atStartOfDay());
        } else {
            q.setParameter("start", null);
            q.setParameter("startAt", null);
        }

        if (endDate != null) {
            q.setParameter("end", endDate);
            q.setParameter("endAt", endDate.atTime(23,59,59));
        } else {
            q.setParameter("end", null);
            q.setParameter("endAt", null);
        }

        List<Object[]> rows = q.getResultList();

        Map<LocalDate, Long> result = new LinkedHashMap<>();
        for (Object[] row : rows) {
            Object dbDate = row[0];
            LocalDate date;
            if (dbDate instanceof java.time.LocalDate) {
                date = (LocalDate) dbDate;
            } else if (dbDate instanceof java.sql.Date) {
                date = ((java.sql.Date) dbDate).toLocalDate();
            } else {
                // phòng trường hợp khác, thử convert qua String
                date = LocalDate.parse(dbDate.toString());
            }
            Long count = (Long) row[1];
            result.put(date, count);
        }
        return result;
    }

    @Override
    public List<OrderItem> findOrdersByOrderItemId(int orderId) {
        String hql = "SELECT ori FROM OrderItem ori " +
                "LEFT JOIN fetch ori.order ord " +
                "LEFT JOIN fetch ori.product p " +
                "WHERE ori.order.order_id = :orderId";
        return currentSession().createQuery(hql, OrderItem.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    @Override
    public long countOrders(LocalDate start, LocalDate end, String keyword) {
        LocalDateTime startDt = (start != null)? start.atStartOfDay() : null;
        LocalDateTime endDt   = (end   != null)? end.atTime(23,59,59): null;


        String hql = "SELECT count(o) FROM Order o WHERE (:s IS NULL OR o.created_at >= :s) " +
                "AND (:e IS NULL OR o.created_at <= :e)";

        boolean hasKeyword = StringUtils.hasText(keyword);
        Integer userId = null;

        if (keyword != null && !keyword.trim().isEmpty()) {
            if (hasKeyword) {
                try {
                    userId = Integer.valueOf(keyword.trim());
                } catch (NumberFormatException ignored) {
                }
            }

            if(userId != null){
                hql += " AND ((:keywordUser IS NOT NULL AND o.user_id = :keywordUser) "
                        + " OR o.payment_id = :keywordPayment)";
            } else {
                hql += " AND o.payment_id = :keywordPayment ";
            }
        }

        Query<Long> q = currentSession().createQuery(hql, Long.class);

        q.setParameter("s", startDt);
        q.setParameter("e", endDt);

        if (keyword != null && !keyword.trim().isEmpty()) {
            if (userId != null) {
                q.setParameter("keywordUser",userId);
            }
            q.setParameter("keywordPayment", keyword.trim());
        }

        return q.uniqueResult();
    }
}
