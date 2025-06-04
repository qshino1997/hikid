package com.example.service.Cart;

import com.example.dao.CartDao;
import com.example.dao.CartItemDAO;
import com.example.dao.UserDAO;
import com.example.dto.CartItemDto;
import com.example.dto.ProductDto;
import com.example.entity.Cart;
import com.example.entity.CartItem;
import com.example.entity.Product;
import com.example.entity.User;
import com.example.service.ProductService;
import com.example.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("databaseCartService")
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class DatabaseCartService implements CartService {
    @Autowired
    private CartDao cartDao;
    @Autowired
    private CartItemDAO cartItemDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ProductService productService;
    @Autowired
    private ModelMapper modelMapper;

    private Cart cart;

    @Override
    /** Khởi tạo cho user cụ thể, tạo Cart mới nếu chưa có */
    public void initForUser(Integer userId) {
        cart = cartDao.findByUserId(userId);
        if(cart == null){
            User user = userDAO.findById(userId);
            Cart newCart = new Cart();
            newCart.setUser(user);
            cartDao.saveOrUpdate(newCart);
            cart = newCart;
        }
    }

    @Override
    public void addItem(ProductDto p, int qty) {
        // Tìm CartItem tương ứng trong DB
        Optional<CartItem> optItem = cartItemDAO.findByCartIdAndProductId(
                cart.getCart_id(), p.getProduct_id()
        );
        if (optItem.isPresent()) {
            CartItem item = optItem.get();
            item.setQuantity(item.getQuantity() + qty);
            cartItemDAO.saveOrUpdate(item);
        } else {
            // Lấy entity Product từ service
            Product product = productService.getProductById(p.getProduct_id());
            // Tạo mới CartItem với association Product
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(qty);
            cartItemDAO.saveOrUpdate(item);
        }
    }

    @Override
    public void updateItem(int productId, int qty) {
        cartItemDAO.findByCartIdAndProductId(cart.getCart_id(), productId)
                .ifPresent(item -> {
                    if (qty <= 0) {
                        cartItemDAO.delete(item);
                    } else {
                        item.setQuantity(qty);
                        cartItemDAO.saveOrUpdate(item);
                    }
                });
    }

    @Override
    public void removeItem(int productId) {
        cartItemDAO.findByCartIdAndProductId(cart.getCart_id(), productId)
                .ifPresent(cartItemDAO::delete);
    }

    @Override
    public Collection<CartItemDto> getItems() {
        return cartItemDAO.findByCartId(cart.getCart_id()).stream()
                .map(ent -> {
                    // map Product entity thành ProductDto
                    ProductDto pd = modelMapper.map(ent.getProduct(), ProductDto.class);
                    return new CartItemDto(pd, ent.getQuantity());
                })
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalQuantity() {
        return getItems().stream().mapToInt(CartItemDto::getQuantity).sum();
    }

    @Override
    public long getTotal() {
        return getItems().stream().mapToLong(CartItemDto::getSubTotal)
                .sum();
    }

    @Override
    public boolean isEmpty() {
        return getItems().isEmpty();
    }

    @Override
    public void clear() {
        cartItemDAO.deleteAllByCart(cart);
    }
}
