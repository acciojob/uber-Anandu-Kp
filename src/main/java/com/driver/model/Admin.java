package com.driver.model;

import javax.persistence.*;

@Entity
@Table(name = "admin")
public class Admin{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int AdminId;

    private String username;

    private String password;

    public Admin() {
    }

    public int getAdminId() {
        return AdminId;
    }

    public void setAdminId(int adminId) {
        AdminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}