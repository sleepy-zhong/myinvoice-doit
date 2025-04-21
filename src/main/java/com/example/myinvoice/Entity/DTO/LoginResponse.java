package com.example.myinvoice.Entity.DTO;

public class LoginResponse {

    private String token;
    private String role;
    private String employeeId;
    private String username;
    private String department;
    private String phone;
    private Long  userId;

    // 保持原有结构不变
    public LoginResponse(String token, String role, String employeeId, String username,String department, String phone, Long userId) {
        this.token = token;
        this.role = role;
        this.employeeId = employeeId;
        this.username = username;
        this.department = department;
        this.phone = phone;
        this.userId = userId;

    }


    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    public String getUsername(){return username;}

    public void setUsername(String username){this.username = username;}

    public String getDepartment(){return department;}

    public void setDepartment(String department){this.department = department;}

    public String getPhone(){return phone;}

    public void setPhone(String phone){this.phone = phone;}

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
