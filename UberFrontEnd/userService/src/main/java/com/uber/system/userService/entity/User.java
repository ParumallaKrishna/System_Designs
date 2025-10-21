package com.uber.system.userService.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL Auto Increment
    private Long id;

    @Column(nullable = false, length = 100) // name required
    private String name;

    @Column(nullable = false, length = 150, unique = true) // email unique
    private String email;

    @Column(length = 15, unique = true) // phone optional but unique if present
    private String phone;

    @Column(nullable = false, length = 255) // hashed password
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20) // store as String
    private Role role;

    public enum Role {
        DRIVER, RIDER
    }

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
