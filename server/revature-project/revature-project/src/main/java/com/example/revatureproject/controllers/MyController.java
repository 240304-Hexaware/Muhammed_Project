package com.example.revatureproject.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.revatureproject.exceptions.InvalidUserException;
import com.example.revatureproject.exceptions.ItemNotFoundException;
import com.example.revatureproject.exceptions.UserAlreadyExistsException;
import com.example.revatureproject.models.User;
import com.example.revatureproject.services.MyService;

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
    public ResponseEntity<User> register(@RequestBody User user) throws UserAlreadyExistsException {

        User registeredUser = service.insertUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);

    }

    /* login */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) throws ItemNotFoundException {
        String token = service.authenticate(user);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // String token = generateToken(loggedUser);
        return ResponseEntity.ok(token);
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
    public ResponseEntity<User> deleteUser(@PathVariable String id) throws ItemNotFoundException{
        try {
            User user = service.removeUser(new ObjectId(id));
            return ResponseEntity.ok(user);
        }
        catch (ItemNotFoundException e) {
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
        }
        catch (ItemNotFoundException e) {
            throw new ItemNotFoundException("User not found");
        }
        
    }

    /*Update role */
    // @PatchMapping("users/admin/updateRole")
    // public ResponseEntity<User> changeRole(@RequestBody List<User> users) throws ItemNotFoundException, InvalidUserException{
    //     try{
    //         User userAdmin = users.get(0);
    //         User user = users.get(1);

    //         User updatedUser = service.updateRole(userAdmin, user);
    //         return ResponseEntity.ok(updatedUser);
    //     }
    //     catch (ItemNotFoundException e) {
    //         throw new ItemNotFoundException("Can't find user to update");
    //     }
    // }
    
    // Exception Handler
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String queryItemNotFound(ItemNotFoundException e) {
        // TODO: change this out for a log message
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