package com.demo.employee.service;

import java.util.Optional;

import com.demo.employee.entity.User;
import com.demo.employee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public boolean authenticate(String username, String password) {

        Optional<User> user=userRepository.findById(username);

        if(user.get().getUsername()!=null && user.get().getPassword().equals(password))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


}
