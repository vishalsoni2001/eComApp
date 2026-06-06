package com.app.ecom.controller;

import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {


    //we can do this using by autowired but here we are doing using @RequiredArgsConstructor
    private final UserService userService;

    @GetMapping
    //@RequestMapping(value = "/api/users",method = RequestMethod.GET)
    public ResponseEntity<List<UserResponse>> getAllUsers()
    {
      return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
      //or
      //return ResponseEntity.ok(userService.fetchAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id)
    {
//        User user=userService.fetchUser(id);
//        if(user==null)
//        {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(user);
//        Replaced above snippet with below code using stream

        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest)
    {
         userService.addUser(userRequest);
         return ResponseEntity.ok("User Added");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id,@RequestBody UserRequest updateUserRequest)
    {
        boolean updated=userService.updateUser(id,updateUserRequest);
        if(updated)
        {
            return ResponseEntity.ok("User Updated Successfully");
        }
        return ResponseEntity.notFound().build();

    }

}
