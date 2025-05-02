package com.Private_Plot.blog_project.entity;

import com.Private_Plot.blog_project.Security.DES;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Entity
@Data
public class User {
    @Id
    private Long id;
    private String username;
    private String password;
    private String InvitationCode;

    // 手动生成唯一的随机 id
    public void generateRandomId() {
        // 使用 UUID 生成一个随机数，然后转换为 Long 类型
        UUID uuid = UUID.randomUUID();
        this.id = uuid.getMostSignificantBits() & Long.MAX_VALUE;
    }

    // 去除 InvitationCode 成员的方法（目的除去在数据库中的存储）
    public void removeInvitationCode() {
        this.InvitationCode = null;
    }


}