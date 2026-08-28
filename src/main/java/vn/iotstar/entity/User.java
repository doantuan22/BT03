package vn.iotstar.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
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
@Table(name = "User")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Email must not be empty")
    @Column(name = "email", columnDefinition = "nvarchar(255) not null")
    private String email;

    @NotEmpty(message = "Username must not be empty")
    @Column(name = "username", columnDefinition = "nvarchar(100) not null")
    private String userName;

    @NotEmpty(message = "Full name must not be empty")
    @Column(name = "fullname", columnDefinition = "nvarchar(255) not null")
    private String fullName;

    @NotEmpty(message = "Password must not be empty")
    @Column(name = "password", columnDefinition = "nvarchar(255) not null")
    private String passWord;

    @Column(name = "avatar", columnDefinition = "nvarchar(500) null")
    private String avatar;

    @Column(name = "roleid", nullable = false)
    private int roleid;

    @NotEmpty(message = "Phone must not be empty")
    @Column(name = "phone", columnDefinition = "nvarchar(30) not null")
    private String phone;

    @Temporal(TemporalType.DATE)
    @Column(name = "createdDate", nullable = false)
    private Date createdDate;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPassWord() { return passWord; }
    public void setPassWord(String passWord) { this.passWord = passWord; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public int getRoleid() { return roleid; }
    public void setRoleid(int roleid) { this.roleid = roleid; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
}
