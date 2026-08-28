package vn.iotstar.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import vn.iotstar.dao.IUserDao;
import vn.iotstar.dao.UserDao;
import vn.iotstar.model.User;
import vn.iotstar.service.UserService;

public class UserServiceImpl implements UserService {
    private final IUserDao userDao = new UserDao();

    @Override
    public User login(String username, String password) {
        User user = this.get(username);
        if (user != null && password.equals(user.getPassWord())) {
            return user;
        }
        return null;
    }

    @Override
    public User get(String username) {
        return toModel(userDao.findByUsername(username));
    }

    @Override
    public boolean register(String username, String password, String email, String fullname, String phone) {
        if (checkExistUsername(username)
                || checkExistEmail(email)
                || checkExistPhone(phone)) {
            return false;
        }
        User user = new User();
        user.setEmail(email);
        user.setUserName(username);
        user.setFullName(fullname);
        user.setPassWord(password);
        user.setAvatar(null);
        user.setRoleid(5);
        user.setPhone(phone);
        user.setCreatedDate(new Date());
        userDao.insert(toEntity(user));
        return true;
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
        return model;
    }

    private vn.iotstar.entity.User toEntity(User model) {
        vn.iotstar.entity.User entity = new vn.iotstar.entity.User();
        entity.setId(model.getId()); entity.setEmail(model.getEmail()); entity.setUserName(model.getUserName());
        entity.setFullName(model.getFullName()); entity.setPassWord(model.getPassWord()); entity.setAvatar(model.getAvatar());
        entity.setRoleid(model.getRoleid()); entity.setPhone(model.getPhone()); entity.setCreatedDate(model.getCreatedDate());
        return entity;
    }
}
