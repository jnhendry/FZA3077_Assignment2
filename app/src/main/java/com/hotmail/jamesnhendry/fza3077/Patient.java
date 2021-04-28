package com.hotmail.jamesnhendry.fza3077;

public class Patient {
    private String clinitianUsername,name,username,password;

    public Patient(String clinitianUsername, String name, String username, String password) {
        this.clinitianUsername = clinitianUsername;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getClinitianUsername() {
        return clinitianUsername;
    }

    public void setClinitianUsername(String clinitianUsername) {
        this.clinitianUsername = clinitianUsername;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
