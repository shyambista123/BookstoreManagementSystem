package com.spring.bookstore.service.order;

import com.spring.bookstore.model.User;
import com.spring.bookstore.model.cart.CartItem;
import com.spring.bookstore.model.order.UserOrder;

import java.util.List;

public interface IOrderService {

    UserOrder placeOrder(User user, CartItem cartItem);
    void cancelOrder(UserOrder order);
    List<UserOrder> getOrdersByUser(User user);


    UserOrder getOrderById(Long orderId);
}
