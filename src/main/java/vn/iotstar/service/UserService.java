package vn.iotstar.service;

import java.util.List;
import vn.iotstar.model.User;

public interface UserService {
    User login(String username, String password);
    User get(String username);

    void insert(User user);
    boolean register(String username, String password, String email, String fullname, String phone);
    boolean verifyOtp(String usernameOrEmail, String otpCode);
    boolean activateAccount(String email, String otpCode);
    boolean resendOtp(String usernameOrEmail);
    boolean requestPasswordReset(String emailOrUsername);
    boolean resetPassword(String emailOrUsername, String otpCode, String newPassword);
    boolean checkExistEmail(String email);
    boolean checkExistUsername(String username);
    boolean checkExistPhone(String phone);
    void update(User user);
    void delete(int id);
    User findById(int id);
    User findByEmail(String email);
    List<User> findAll();
    List<User> findAll(int page, int pageSize);
    List<User> searchByKeyword(String keyword);
    int count();
}
