package com.spring.bookstore.service.order;

import com.spring.bookstore.model.User;
import com.spring.bookstore.model.cart.CartItem;
import com.spring.bookstore.model.order.OrderItem;
import com.spring.bookstore.model.order.UserOrder;
import com.spring.bookstore.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;

    @Override
    public UserOrder placeOrder(User user, CartItem cartItem) {
        // Create an Order instance and populate its attributes

        UserOrder order = new UserOrder();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        // Calculate total amount based on cart item
        Double totalAmount = cartItem.getBook().getPrice() * cartItem.getQuantity();
        order.setTotalAmount(totalAmount);

        // Create and associate an OrderItem with the order
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setTotalPrice(cartItem.getBook().getPrice() * cartItem.getQuantity());
        order.getOrderItems().add(orderItem);

        // Save the order and order items in the repository
        orderRepository.save(order);

        return order;
    }


    @Override
    public List<UserOrder> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public void cancelOrder(UserOrder order) {
        orderRepository.delete(order);
    }

    @Override
    public UserOrder getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

}
