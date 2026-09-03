package vn.iotstar.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u ORDER BY u.id DESC")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Email must not be empty")
    @Column(name = "email", columnDefinition = "nvarchar(255) not null", nullable = false, unique = true)
    private String email;

    @NotEmpty(message = "Username must not be empty")
    @Column(name = "username", columnDefinition = "nvarchar(100) not null", nullable = false, unique = true)
    private String username;

    @NotEmpty(message = "Full name must not be empty")
    @Column(name = "fullname", columnDefinition = "nvarchar(255) not null")
    private String fullName;

    @NotEmpty(message = "Password must not be empty")
    @Column(name = "password", columnDefinition = "nvarchar(255) not null")
    private String password;

    @Column(name = "image_url", columnDefinition = "nvarchar(500) null")
    private String imageUrl;

    @Column(name = "image_public_id", columnDefinition = "nvarchar(255) null")
    private String imagePublicId;

    @Column(name = "role_id", nullable = false)
    private int roleId = 5;

    @NotEmpty(message = "Phone must not be empty")
    @Column(name = "phone", columnDefinition = "nvarchar(30) not null")
    private String phone;

    @Column(name = "otp_code", columnDefinition = "nvarchar(10) null")
    private String otpCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "otp_expires_at", nullable = true)
    private Date otpExpiresAt;

    @Column(name = "is_activated", nullable = false, columnDefinition = "bit default 0")
    private boolean isActivated = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = new Date();
        }
        if (updatedAt == null) {
            updatedAt = new Date();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
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

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

    public Date getOtpExpiresAt() { return otpExpiresAt; }
    public void setOtpExpiresAt(Date otpExpiresAt) { this.otpExpiresAt = otpExpiresAt; }

    public boolean isActivated() { return isActivated; }
    public void setActivated(boolean isActivated) { this.isActivated = isActivated; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
