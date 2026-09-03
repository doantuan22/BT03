package vn.iotstar.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String email;
    private String username;
    private String fullName;
    private String password;
    private String imageUrl;
    private String imagePublicId;
    private int roleId = 5;
    private String phone;
    private Date createdAt;
    private Date updatedAt;
    private String otpCode;
    private Date otpExpiresAt;
    private boolean isActivated;

    public User() { }

    public User(String email, String username, String fullName, String password, String imageUrl, String imagePublicId, int roleId, String phone, Date createdAt) {
        this.email = email;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.imageUrl = imageUrl;
        this.imagePublicId = imagePublicId;
        this.roleId = roleId;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public User(String email, String username, String fullName, String password, String imageUrl, String imagePublicId, int roleId, String phone, Date createdAt, String otpCode, Date otpExpiresAt, boolean isActivated) {
        this.email = email;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.imageUrl = imageUrl;
        this.imagePublicId = imagePublicId;
        this.roleId = roleId;
        this.phone = phone;
        this.createdAt = createdAt;
        this.otpCode = otpCode;
        this.otpExpiresAt = otpExpiresAt;
        this.isActivated = isActivated;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getImagePublicId() { return imagePublicId; }
    public void setImagePublicId(String imagePublicId) { this.imagePublicId = imagePublicId; }

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

    public Date getOtpExpiresAt() { return otpExpiresAt; }
    public void setOtpExpiresAt(Date otpExpiresAt) { this.otpExpiresAt = otpExpiresAt; }

    public boolean isActivated() { return isActivated; }
    public void setActivated(boolean isActivated) { this.isActivated = isActivated; }
}
