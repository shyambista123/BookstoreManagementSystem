package com.spring.bookstore.service.cart;

import com.spring.bookstore.model.User;
import com.spring.bookstore.model.book.Book;
import com.spring.bookstore.model.cart.CartItem;
import com.spring.bookstore.repository.cart.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartService implements ICartService{
    private final CartItemRepository cartItemRepository;
    @Override
    public List<CartItem> getCartItems() {
        return cartItemRepository.findAll();
    }

    @Override
    public void addToCart(User user, Book book, int quantity) {
        Optional<CartItem> optionalCartItem = cartItemRepository.findByUserAndBook(user, book);
        if (optionalCartItem.isPresent()) {
            // If the book is already in the cart, update the quantity
            CartItem cartItem = optionalCartItem.get();
            int newQuantity = cartItem.getQuantity() + quantity;

            // Check if new quantity exceeds available stock
            if (newQuantity <= book.getQuantityInStock()) {
                cartItem.setQuantity(newQuantity);
            } else {
                // Handle out-of-stock scenario (e.g., show an error message)
                throw new RuntimeException("Cannot add more than available stock");
            }
        } else {
            // If the book is not in the cart, create a new cart item
            if (quantity <= book.getQuantityInStock()) {
                CartItem cartItem = new CartItem();
                cartItem.setBook(book);
                cartItem.setQuantity(quantity);
                cartItem.setUser(user);
                cartItemRepository.save(cartItem);
            } else {
                // Handle out-of-stock scenario (e.g., show an error message)
                throw new RuntimeException("Cannot add more than available stock");
            }
        }
    }

    @Override
    public List<CartItem> getCartItemsByUser(User user) {
        return cartItemRepository.findByUser(user);
    }


    @Override
    public void removeCartItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }
}
