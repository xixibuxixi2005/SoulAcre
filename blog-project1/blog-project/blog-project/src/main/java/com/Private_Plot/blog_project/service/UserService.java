package com.Private_Plot.blog_project.service;

import com.Private_Plot.blog_project.entity.User;
import com.Private_Plot.blog_project.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.Private_Plot.blog_project.Security.DES;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private Map<String, String> invitationCode=new HashMap<>();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        /**
         * 生成用户
         *
         * @param USER 用户类
         * @return 数据库是否成功存储
         */

        do {
            user.generateRandomId();
        } while (userRepository.existsById(user.getId()));//确保id唯一

        user.removeInvitationCode();//去除无关信息

        return userRepository.save(user);
    }

    public boolean isUsernameExists(String username) {
        /**
         * 寻找该用户是否存在
         *
         * @param String username 用户名
         * @return boolean 数据库是否找到该用户
         */
        return userRepository.findByUsername(username) != null;
    }

    public void setinvitationCode(String username) throws Exception {
        /**
         * 生成相应的邀请码，并暂时存储在invitationCode表中
         *
         * @param String username 用户名
         */
        String invitation=DES.generateKey();
        invitationCode.put(username, invitation);
    }

    public String getInvitationCode(String username) {
        /**
         * 返回相应的邀请码
         *
         * @param String username 用户名
         * @return String 邀请码
         */
        return invitationCode.get(username);
    }
}