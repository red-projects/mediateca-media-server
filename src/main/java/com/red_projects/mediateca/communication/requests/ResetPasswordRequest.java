package com.red_projects.mediateca.communication.requests;

public class ResetPasswordRequest {

    private ResetMethod resetMethod;
    private String username;
    private String password;
    private String newPassword;

    public ResetMethod getResetMethod() {
        return resetMethod;
    }

    public void setResetMethod(ResetMethod resetMethod) {
        this.resetMethod = resetMethod;
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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
