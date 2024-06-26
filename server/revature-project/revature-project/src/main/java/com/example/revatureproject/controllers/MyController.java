package com.example.revatureproject.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.revatureproject.exceptions.ItemNotFoundException;
import com.example.revatureproject.exceptions.UserAlreadyExistsException;
import com.example.revatureproject.models.User;
import com.example.revatureproject.services.MyService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class MyController {

    private MyService service;

    @Autowired
    public MyController(MyService service) {
        System.out.println("Controller Constructor");
        this.service = service;
    }

    /* registration */
    @PostMapping("/registration")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<User> register(@RequestBody User user) throws UserAlreadyExistsException {
        User registeredUser = service.insertUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);

    }

    /* login */
    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<String> login(@RequestBody User user, HttpServletResponse response)
            throws ItemNotFoundException {
        try {
            String token = service.authenticate(user);
            if (token != null) {
                // Create a cookie
                Cookie authCookie = new Cookie("token", token);
                authCookie.setHttpOnly(true);
                authCookie.setMaxAge(24 * 60 * 60); // expires in 1 day
                authCookie.setPath("/");

                response.addCookie(authCookie);
                return ResponseEntity.ok().body("Login Successful. Token set in cookie.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Failed");
            }
        } catch (ItemNotFoundException e) {
            return ResponseEntity.ok().body("Login Failed");

        }
    }

    @PostMapping("/logout")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie authCookie = new Cookie("AUTH_TOKEN", null);
        authCookie.setPath("/");
        authCookie.setHttpOnly(true);
        authCookie.setMaxAge(0); // Invalidate the cookie
        response.addCookie(authCookie);
        return ResponseEntity.ok("Logged out successfully");
    }

    /* find all users */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = new ArrayList<>();
        users = service.findAllUsers();
        if (users.size() == 0) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/users/deletion/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable String id) throws ItemNotFoundException {
        try {
            User user = service.removeUser(new ObjectId(id));
            return ResponseEntity.ok(user);
        } catch (ItemNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Path Variable, QueryParam
    // www.site.com/context-path/users/?firstName=kyle

    @GetMapping("/users/id/{id}")
    public ResponseEntity<User> getById(@PathVariable String id) throws ItemNotFoundException {
        try {
            User user = service.findById(new ObjectId(id));
            return ResponseEntity.ok(user);
        } catch (ItemNotFoundException e) {
            throw new ItemNotFoundException("User not found");
        }

    }

    // Exception Handler
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String queryItemNotFound(ItemNotFoundException e) {
        System.out.println(e.getMessage());// we would want to log this instead in the real world
        return e.getMessage();
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String queryUserAlreadyExists(UserAlreadyExistsException e) {
        System.out.println(e.getMessage());
        return e.getMessage();
    }

}