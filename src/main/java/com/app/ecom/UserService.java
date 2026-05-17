package com.app.ecom;

import com.app.ecom.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private Long count= 1L;
    private List<User> userList =new ArrayList<>();

    public List<User> fetchAllUsers()
    {
        return userList;
    }

    public void addUser(User user)
    {
        user.setId(count++);
        userList.add(user);

    }


    public Optional<User> fetchUser(Long id) {

//        for(User user : userList)
//        {
//          if(user.getId().equals(id))
//          {
//                return user;
//          }
//
//        }
//        return null;
//        Replaced above snippet with below code using stream

        return userList.stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    public boolean updateUser(Long id,User updatedUser)
    {
        return userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .map(existingUser -> {
                    existingUser.setFirstName(updatedUser.getFirstName());
                    existingUser.setLastName(updatedUser.getLastName());
                    return true;
                    }).orElse(false);
    }
}
