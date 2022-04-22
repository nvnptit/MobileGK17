package com.nvn.mobilegk17.model;

public class User {
    String email, password, name, image,phone,salt;
    int isVerify;

    public User(String email, String password, String name, String image, String phone, String salt, int isVerify) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.salt = salt;
        this.isVerify = isVerify;
    }
    public User(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getIsVerify() {
        return isVerify;
    }

    public void setIsVerify(int isVerify) {
        this.isVerify = isVerify;
    }
}
