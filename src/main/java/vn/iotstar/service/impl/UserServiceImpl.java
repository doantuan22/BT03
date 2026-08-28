package vn.iotstar.service.impl;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import vn.iotstar.dao.IUserDao;
import vn.iotstar.dao.UserDao;
import vn.iotstar.model.User;
import vn.iotstar.service.EmailService;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.EmailServiceImpl;
import vn.iotstar.util.Constant;

public class UserServiceImpl implements UserService {
    private final IUserDao userDao = new UserDao();
    private final EmailService emailService = new EmailServiceImpl();

    @Override
    public User login(String username, String password) {
        User user = this.get(username);
        if (user != null && password.equals(user.getPassWord())) {
            // User cannot log in until activated
            if (!user.isActivated()) {
                return null;
            }
            return user;
        }
        return null;
    }

    @Override
    public User get(String username) {
        return toModel(userDao.findByUsername(username));
    }

    @Override
    public User findByEmail(String email) {
        return toModel(userDao.findByEmail(email));
    }

    @Override
    public boolean register(String username, String password, String email, String fullname, String phone) {
        if (checkExistUsername(username)
                || checkExistEmail(email)
                || checkExistPhone(phone)) {
            return false;
        }

        String otpCode = generateOtp();
        long expiryMs = System.currentTimeMillis() + (Constant.OTP_EXPIRY_MINUTES * 60 * 1000L);
        Date otpExpiresAt = new Date(expiryMs);

        User user = new User();
        user.setEmail(email);
        user.setUserName(username);
        user.setFullName(fullname);
        user.setPassWord(password);
        user.setAvatar(null);
        user.setRoleid(5);
        user.setPhone(phone);
        user.setCreatedDate(new Date());
        user.setOtpCode(otpCode);
        user.setOtpExpiresAt(otpExpiresAt);
        user.setActivated(false);

        userDao.insert(toEntity(user));

        // Dispatch activation email with 6-digit OTP
        emailService.sendActivationEmail(email, fullname, otpCode);

        return true;
    }

    @Override
    public boolean verifyOtp(String usernameOrEmail, String otpCode) {
        if (usernameOrEmail == null || otpCode == null || otpCode.isBlank()) {
            return false;
        }
        vn.iotstar.entity.User entity = userDao.findByUsername(usernameOrEmail);
        if (entity == null) {
            entity = userDao.findByEmail(usernameOrEmail);
        }
        if (entity == null) {
            return false;
        }
        if (entity.isActivated()) {
            return true;
        }
        if (entity.getOtpCode() == null || !entity.getOtpCode().equals(otpCode.trim())) {
            return false;
        }
        if (entity.getOtpExpiresAt() == null || entity.getOtpExpiresAt().before(new Date())) {
            return false;
        }

        entity.setActivated(true);
        entity.setOtpCode(null);
        entity.setOtpExpiresAt(null);
        userDao.update(entity);
        return true;
    }

    @Override
    public boolean activateAccount(String email, String otpCode) {
        return verifyOtp(email, otpCode);
    }

    @Override
    public boolean resendOtp(String usernameOrEmail) {
        if (usernameOrEmail == null || usernameOrEmail.isBlank()) {
            return false;
        }
        vn.iotstar.entity.User entity = userDao.findByUsername(usernameOrEmail);
        if (entity == null) {
            entity = userDao.findByEmail(usernameOrEmail);
        }
        if (entity == null || entity.isActivated()) {
            return false;
        }

        String otpCode = generateOtp();
        long expiryMs = System.currentTimeMillis() + (Constant.OTP_EXPIRY_MINUTES * 60 * 1000L);
        entity.setOtpCode(otpCode);
        entity.setOtpExpiresAt(new Date(expiryMs));
        userDao.update(entity);

        emailService.sendActivationEmail(entity.getEmail(), entity.getFullName(), otpCode);
        return true;
    }

    @Override
    public boolean requestPasswordReset(String emailOrUsername) {
        if (emailOrUsername == null || emailOrUsername.isBlank()) {
            return false;
        }
        vn.iotstar.entity.User entity = userDao.findByEmail(emailOrUsername.trim());
        if (entity == null) {
            entity = userDao.findByUsername(emailOrUsername.trim());
        }
        if (entity == null) {
            return false;
        }

        String otpCode = generateOtp();
        long expiryMs = System.currentTimeMillis() + (Constant.OTP_EXPIRY_MINUTES * 60 * 1000L);
        entity.setOtpCode(otpCode);
        entity.setOtpExpiresAt(new Date(expiryMs));
        userDao.update(entity);

        emailService.sendPasswordResetEmail(entity.getEmail(), entity.getFullName(), otpCode);
        return true;
    }

    @Override
    public boolean resetPassword(String emailOrUsername, String otpCode, String newPassword) {
        if (emailOrUsername == null || otpCode == null || newPassword == null
                || emailOrUsername.isBlank() || otpCode.isBlank() || newPassword.isBlank()) {
            return false;
        }
        vn.iotstar.entity.User entity = userDao.findByEmail(emailOrUsername.trim());
        if (entity == null) {
            entity = userDao.findByUsername(emailOrUsername.trim());
        }
        if (entity == null) {
            return false;
        }
        if (entity.getOtpCode() == null || !entity.getOtpCode().equals(otpCode.trim())) {
            return false;
        }
        if (entity.getOtpExpiresAt() == null || entity.getOtpExpiresAt().before(new Date())) {
            return false;
        }

        entity.setPassWord(newPassword);
        entity.setOtpCode(null);
        entity.setOtpExpiresAt(null);
        entity.setActivated(true);
        userDao.update(entity);
        return true;
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(1000000);
        return String.format("%06d", code);
    }

    @Override
    public boolean checkExistEmail(String email) {
        return userDao.findByEmail(email) != null;
    }

    @Override
    public boolean checkExistUsername(String username) {
        return userDao.findByUsername(username) != null;
    }

    @Override
    public boolean checkExistPhone(String phone) {
        return userDao.findByPhone(phone) != null;
    }

    @Override
    public void insert(User user) {
        userDao.insert(toEntity(user));
    }

    @Override
    public void update(User user) {
        if (userDao.findById(user.getId()) == null) throw new IllegalArgumentException("User does not exist");
        userDao.update(toEntity(user));
    }

    @Override
    public void delete(int id) {
        try { userDao.delete(id); }
        catch (Exception e) { throw new IllegalStateException("Cannot delete user", e); }
    }

    @Override
    public User findById(int id) { return toModel(userDao.findById(id)); }

    @Override
    public List<User> findAll() { return userDao.findAll().stream().map(this::toModel).collect(Collectors.toList()); }

    @Override
    public List<User> findAll(int page, int pageSize) { return userDao.findAll(page, pageSize).stream().map(this::toModel).collect(Collectors.toList()); }

    @Override
    public List<User> searchByKeyword(String keyword) { return userDao.searchByKeyword(keyword).stream().map(this::toModel).collect(Collectors.toList()); }

    @Override
    public int count() { return userDao.count(); }

    private User toModel(vn.iotstar.entity.User entity) {
        if (entity == null) return null;
        User model = new User();
        model.setId(entity.getId()); model.setEmail(entity.getEmail()); model.setUserName(entity.getUserName());
        model.setFullName(entity.getFullName()); model.setPassWord(entity.getPassWord()); model.setAvatar(entity.getAvatar());
        model.setRoleid(entity.getRoleid()); model.setPhone(entity.getPhone()); model.setCreatedDate(entity.getCreatedDate());
        model.setOtpCode(entity.getOtpCode()); model.setOtpExpiresAt(entity.getOtpExpiresAt()); model.setActivated(entity.isActivated());
        return model;
    }

    private vn.iotstar.entity.User toEntity(User model) {
        vn.iotstar.entity.User entity = new vn.iotstar.entity.User();
        entity.setId(model.getId()); entity.setEmail(model.getEmail()); entity.setUserName(model.getUserName());
        entity.setFullName(model.getFullName()); entity.setPassWord(model.getPassWord()); entity.setAvatar(model.getAvatar());
        entity.setRoleid(model.getRoleid()); entity.setPhone(model.getPhone()); entity.setCreatedDate(model.getCreatedDate());
        entity.setOtpCode(model.getOtpCode()); entity.setOtpExpiresAt(model.getOtpExpiresAt()); entity.setActivated(model.isActivated());
        return entity;
    }
}
