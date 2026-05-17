package com.app.ecom;

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
    //Below snippet is using request mapping at method level
    //@RequestMapping(value = "/api/users",method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAllUsers()
    {
      return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
      //or
      //return ResponseEntity.ok(userService.fetchAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id)
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
    public ResponseEntity<String> createUser(@RequestBody User user)
    {
         userService.addUser(user);
         return ResponseEntity.ok("User Added");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id,@RequestBody User updatedUser)
    {
        boolean updated=userService.updateUser(id,updatedUser);
        if(updated)
        {
            return ResponseEntity.ok("User Updated Successfully");
        }
        return ResponseEntity.notFound().build();

    }

}
