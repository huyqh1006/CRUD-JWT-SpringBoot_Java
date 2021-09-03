package com.example.training.controller;

import com.example.training.entity.User;
import com.example.training.service.implementation.IUserServiceImpl;
import com.example.training.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("")
    public ResponseEntity<Iterable<User>> getAllUser(){
        return new ResponseEntity<>(userService.findAll(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id){
        Optional<User> userOptional = userService.findById(id);
        return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("")
    public ResponseEntity<User> createNewUser(@RequestBody User user){
        return new ResponseEntity<>(userService.save(user),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,@RequestBody User user){
        Optional<User> userOptional = userService.findById(id);
        return userOptional.map(userupdate -> {user.setId(userupdate.getId());
            return new ResponseEntity<>(userService.save(user),HttpStatus.OK);})
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping( "/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long id){
        Optional<User> userOptional = userService.findById(id);
        return userOptional.map(user -> {
            userService.delete(id);
            return new ResponseEntity<>(user,HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
