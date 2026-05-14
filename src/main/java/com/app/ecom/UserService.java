package com.app.ecom;

import com.app.ecom.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public User fetchUser(Long id) {
        for(User user : userList)
        {
          if(user.getId().equals(id))
          {
                return user;
          }

        }
        return null;
    }
}
