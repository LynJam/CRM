package cn.edu.scnu.service.impl;

import cn.edu.scnu.pojo.User;
import cn.edu.scnu.repository.UserRepository;
import cn.edu.scnu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public void addNewUser(User user) {
       userRepository.addUser(user);
    }
}
