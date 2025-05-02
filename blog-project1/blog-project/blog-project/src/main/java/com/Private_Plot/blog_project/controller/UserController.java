package com.Private_Plot.blog_project.controller;

import com.Private_Plot.blog_project.repository.UserRepository;
import com.Private_Plot.blog_project.entity.User;
import com.Private_Plot.blog_project.service.UserService;
import com.Private_Plot.blog_project.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/generateInvitationCode")
    public ResponseEntity<Map<String, String>> generateInvitationCode(@RequestBody User user) {
        Map<String, String> responseBody = new HashMap<>();
        try {
            if (userService.isUsernameExists(user.getUsername())) {
                responseBody.put("error", "Username already exists");
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }

            userService.setinvitationCode(user.getUsername());
            String separator = "============================";
            System.out.println(separator);
            System.out.println(ANSI_YELLOW + user.getUsername() + "的邀请码是  " + userService.getInvitationCode(user.getUsername()) + ANSI_RESET);
            System.out.println(separator);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            responseBody.put("error", "Failed to generate invitation code");
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user, HttpServletResponse response) {
        Map<String, String> responseBody = new HashMap<>();
        if (userService.isUsernameExists(user.getUsername())) {
            responseBody.put("error", "Username already exists, cannot register again");
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }
        // 验证邀请码
        String storedInvitationCode = userService.getInvitationCode(user.getUsername());
        if (storedInvitationCode == null || !storedInvitationCode.equals(user.getInvitationCode())) {
            responseBody.put("error", "Invalid invitation code(错误的邀请码)");
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }

        if(user.getUsername().length() < 4 || user.getUsername().length()>20||
            user.getPassword().length() < 4 || user.getPassword().length()>20){
            responseBody.put("error", "请勿修改前端界面（用户名或密码长度违规）");
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }

        User registeredUser = userService.registerUser(user);
        String token = JwtUtils.generateToken(user.getUsername());
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        response.addCookie(cookie);
        responseBody.put("success", "注册成功");
        responseBody.put("token", token);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user, HttpServletResponse response) {
        String username = user.getUsername();
        String password = user.getPassword();

        // 从数据库中查询用户信息
        User have_user = userRepository.findByUsername(username);

        if (have_user != null && have_user.getPassword().equals(password)) {
            String token = JwtUtils.generateToken(username);
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            response.addCookie(cookie);

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("success", "登录成功");
            responseBody.put("token", token);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("error", "用户名或密码错误");
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        }
    }
}