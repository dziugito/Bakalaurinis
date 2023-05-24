package com.example.draudiminiai_ivykiai.dataStructure;

public class Users {
    String id;
    String name;
    String second_name;
    String email;
    Integer phone_number;
    String username;
    String password;
    Integer is_admin;

    public Users() {
    }

    public Users(String id, String name, String second_name, String email, Integer phone_number, String username, String password, Integer is_admin) {
        this.id = id;
        this.name = name;
        this.second_name = second_name;
        this.email = email;
        this.phone_number = phone_number;
        this.username = username;
        this.password = password;
        this.is_admin = is_admin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(Integer phone_number) {
        this.phone_number = phone_number;
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

    public Integer getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(Integer is_admin) {
        this.is_admin = is_admin;
    }
}
