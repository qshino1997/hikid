<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="card mb-4">
    <div class="card-header bg-secondary text-white">
        <i class="bi bi-basket"></i> Quản lý Đơn hàng
    </div>
    <div class="card-body">
        <!-- Biểu mẫu thống kê -->
        <form id="filterForm" class="row g-3 mb-4">
            <div class="col-md-4">
                <label>Ngày bắt đầu</label>
                <input type="date" name="startDate" value="${startDateDefault}" class="form-control"/>
            </div>
            <div class="col-md-4">
                <label>Ngày kết thúc</label>
                <input type="date" name="endDate" value="${endDateDefault}" class="form-control"/>
            </div>
            <div class="col-md-4">
                <label>Tìm theo User ID / Payment ID</label>
                <input type="text" name="keyword" value="${keywordDefault}" placeholder="Nhập tên hoặc ID" class="form-control"/>
            </div>
            <div class="col-md-4 d-flex align-items-end">
                <button type="submit" class="btn btn-primary">Lọc đơn hàng</button>
            </div>
        </form>

        <!-- Bảng dữ liệu đơn hàng -->
        <!-- Phần bảng: nếu ajax param hiện hữu chỉ in table, nếu không thì in đầy đủ -->
        <div id="ordersTable">
        </div>

        <div class="mb-4">
            <canvas id="orderChart" width="400" height="200"></canvas>
        </div>
    </div>
</div>

<!-- Modal hiển thị thông tin người dùng và đơn hàng -->
<div class="modal fade" id="orderDetailsModal" tabindex="-1" aria-labelledby="orderDetailsModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Chi tiết đơn hàng</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
            </div>
            <div class="modal-body" id="orderDetailsContent">
                <!-- Nội dung sẽ được load bằng AJAX -->
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/resources/js/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script>
    $(function(){
        const base = '${pageContext.request.contextPath}';
        const ajaxUrl  = base + '/admin/order/ajax';
        const statsUrl = base + '/admin/order/stats';
        let currentPage = 1;

        function getFilters() {
            // lấy đúng yyyy-MM-dd từ input, không serialize() lung tung
            return {
                startDate: $('input[name="startDate"]').val(),
                endDate:   $('input[name="endDate"]').val(),
                page:      currentPage,
                size:      5,
                keyword: $('input[name="keyword"]').val()
            };
        }

        function loadOrders(page = 1) {
            const f = getFilters();
            f.page = page;
            f.size = 5; // hoặc 10, số dòng mỗi trang
            $.get(ajaxUrl, f)
                .done(html => $('#ordersTable').html(html))
                .fail((xhr, s) => alert('Lỗi tải đơn hàng: ' + s));
        }

        function loadStats() {
            const f = getFilters();
            $.getJSON(statsUrl, f)
                .done(data => {
                    const labels = data.map(o=>o.date);
                    const counts = data.map(o=>o.count);
                    const ctx2   = document.getElementById('orderChart').getContext('2d');
                    if (window.orderChart?.destroy) window.orderChart.destroy();
                    window.orderChart = new Chart(ctx2, {
                        type: 'bar',
                        data: { labels, datasets:[{ label:'Số đơn', data:counts }] },
                        options:{ scales:{ y:{ beginAtZero:true } } }
                    });
                })
                .fail((xhr,s)=>alert('Lỗi tải thống kê: '+s));
        }

        // Bind event submit
        $('#filterForm').on('submit', function(e){
            e.preventDefault();
            loadOrders(1);
            loadStats();
        });

        // Delegate click cho pagination links
        $(document).on('click', '#ordersTable .pagination .page-link', function(e){
            e.preventDefault();
            const page = $(this).data("page");
            if (!page || $(this).parent().hasClass("disabled") || $(this).parent().hasClass("active")) return;
            loadOrders(page);
        });

        // Load mặc định ngay khi DOM sẵn sàng
        loadOrders(1);
        loadStats();

        $(document).on('click', '.viewUserOrder', function(e){
            e.preventDefault();
            // Hiển thị modal
            const userId = $(this).data('userId');
            const orderId =   $(this).data('orderId');
            const orderDetailUrl = base + '/admin/order/' + orderId + '/' + userId + '/details';

            console.log("User ID:", userId, "Order ID:", orderId);

            $.get(orderDetailUrl)
                .done(html => {
                    $('#orderDetailsContent').html(html);
                    $('#orderDetailsModal').modal('show');
                })
                .fail((xhr, s) => alert('Lỗi tải chi tiet đơn hàng: ' + s));

        });
    });
</script>