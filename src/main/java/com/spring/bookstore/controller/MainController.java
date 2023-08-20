package com.spring.bookstore.controller;

import com.spring.bookstore.model.User;
import com.spring.bookstore.model.book.Book;
import com.spring.bookstore.service.UserService;
import com.spring.bookstore.service.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private final BookService bookService;
    @GetMapping
    public String index(Model model){
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "index";
    }


    @GetMapping("/users")
    public List<User> getALlUsers(){
        return userService.getUser();
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
//    @PostMapping("/login")
//    public String loginSuccess(Model model) {
//        model.addAttribute("message", "Welcome! You have logged in successfully.");
//        return "redirect:/index";
//    }
//    @GetMapping("/cart")
//    public String cart(Model model) {
//        model.addAttribute("book", new Book());
//        return "cart";
//    }
}
