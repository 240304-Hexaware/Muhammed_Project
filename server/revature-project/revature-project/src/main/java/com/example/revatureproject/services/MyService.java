package com.example.revatureproject.services;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.revatureproject.auth.JwtTokenProvider;
import com.example.revatureproject.exceptions.InvalidUserException;
import com.example.revatureproject.exceptions.ItemNotFoundException;
import com.example.revatureproject.exceptions.UserAlreadyExistsException;
import com.example.revatureproject.models.User;
import com.example.revatureproject.repositories.MyRepository;


@Service
public class MyService {

    private MyRepository repository;
    private JwtTokenProvider tokenProvider;

    @Autowired
    public MyService(MyRepository repository, JwtTokenProvider tokenProvider) {
        System.out.println("Service Constructor");
        this.repository = repository;
        this.tokenProvider = tokenProvider;
    }

    /* insert User to collection */
    public User insertUser(User user) throws UserAlreadyExistsException {

        Optional<User> existingUserOptional = repository.findByUsername(user.getUsername());
        if(existingUserOptional.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        newUser.setRole(user.getRole());
        return repository.save(newUser);
    }

    /* log in user */
    public String authenticate(User user) throws ItemNotFoundException{
        Optional<User> existingUser = repository.findByUsername(user.getUsername());
        if(existingUser.isPresent()) {
            User authUser = existingUser.get();
            if(BCrypt.checkpw(user.getPassword(), authUser.getPassword())) {
                return tokenProvider.generateToken(authUser.getUsername(), authUser.getRole());
            }
        }
        throw new ItemNotFoundException("User not found");
    }

    /*find all users */
    public List<User> findAllUsers() {
        return repository.findAll();
    }

    /*delete a user */
    public User removeUser(ObjectId id) throws ItemNotFoundException{ 
        User user = findById(id);
        if(repository.findById(id).isPresent()) {
            repository.deleteById(id);
            return user;
        }
        throw new ItemNotFoundException("User not found for delete");
    }
    /* find a user */
    public User findById(ObjectId id) throws ItemNotFoundException {
        /*
        This returns our result of one exists, otherwise it throws ItemNotFound
         */
        return repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Query returned no result."));
    }

    /* update user role */
    // public User updateRole(User userAdmin, User user) throws InvalidUserException, ItemNotFoundException {
    //     if(userAdmin.getRole() == "user") { throw new InvalidUserException("User not admin, cannot update role"); }
        
    //     Optional<User> userOptional = repository.findById(user.get_id());
    //     if(userOptional.isPresent()) {
    //         System.out.println("INSIDE BLOCK");
    //         User updatedUser = userOptional.get();
    //         System.out.println(updatedUser.getRole());
    //         if(updatedUser.getRole() != "user") {
    //             updatedUser.setRole("admin");
    //             System.out.println("CHANGED TO ADMIN");
    //         }
    //         else{
    //             updatedUser.setRole("user");
    //             System.out.println("CHANGED TO USER");

    //         }
    //         repository.save(updatedUser);
    //         return updatedUser;

    //     }
        
    //     throw new ItemNotFoundException("User not found");
        
        
    // }

    // public Associate findByFirstName(String firstName) throws ItemNotFoundException{
    //     return repository.findByFirstName(firstName).orElseThrow(() -> new ItemNotFoundException("Query returned no result."));
    // }
}