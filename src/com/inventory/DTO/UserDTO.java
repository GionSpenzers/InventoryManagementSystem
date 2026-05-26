/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventory.DTO;

/**
 *
 * @author GKV
 */
public class UserDTO {
    private int id;
    private String fullName, location, phone, username, password, userType;
    private String inTime, outTime;
    private String originalUsername;  
    
    public String getInTime() {
        return inTime;
    }
    
    public void setInTime(String inTime) {
        this.inTime = inTime;
    }
    
    public String getOutTime() {
        return outTime;
    }
    
    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
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
    
    public String getUserType() {
        return userType;
    }
    
    public void setUserType(String userType) {
        this.userType = userType;
    }
    
    public String getOriginalUsername() {
        return originalUsername;
    }
    
    public void setOriginalUsername(String originalUsername) {
        this.originalUsername = originalUsername;
    }
    
}
