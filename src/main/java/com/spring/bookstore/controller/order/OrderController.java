package com.spring.bookstore.controller.order;


import com.spring.bookstore.model.User;
import com.spring.bookstore.model.book.Book;
import com.spring.bookstore.model.cart.CartItem;
import com.spring.bookstore.model.order.OrderItem;
import com.spring.bookstore.model.order.UserOrder;
import com.spring.bookstore.service.UserService;
import com.spring.bookstore.service.book.BookService;
import com.spring.bookstore.service.cart.CartService;
import com.spring.bookstore.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final CartService cartService;
    private final OrderService orderService;
    private final UserService userService;

    @PostMapping("/place-order")
    public String placeOrder(Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get the first book in the cart (if it exists)
        Optional<CartItem> firstCartItem = cartService.getCartItemsByUser(user).stream().findFirst();

        if (firstCartItem.isEmpty()) {
            model.addAttribute("errorMessage", "Your cart is empty.");
            return "cart"; // Return to cart page with an error message
        }

        CartItem cartItem = firstCartItem.get();

        UserOrder order = orderService.placeOrder(user, cartItem);
        cartService.removeCartItem(cartItem.getId());

        model.addAttribute("order", order);
        return "redirect:/order"; // Show the order confirmation page
    }



    @GetMapping
    public String myOrders(Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<UserOrder> orders = orderService.getOrdersByUser(user);
        model.addAttribute("orders", orders);
        return "myOrders";
    }

    @PostMapping("/cancel/{orderId}")
    public String cancelOrder(@PathVariable Long orderId) {
        UserOrder order = orderService.getOrderById(orderId);

        if (order != null) {
            orderService.cancelOrder(order);
            return "redirect:/order";
        } else {
            // Handle the case where the order doesn't exist
            return "redirect:/order";
        }
    }
}
