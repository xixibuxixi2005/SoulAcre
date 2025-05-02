package com.Private_Plot.blog_project.repository;

import com.Private_Plot.blog_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}