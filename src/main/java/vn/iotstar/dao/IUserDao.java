package vn.iotstar.dao;

import java.util.List;
import vn.iotstar.entity.User;

public interface IUserDao {
    void insert(User user);
    void update(User user);
    void delete(int id) throws Exception;
    User findById(int id);
    User findByUsername(String username);
    User findByEmail(String email);
    User findByPhone(String phone);
    List<User> findAll();
    List<User> findAll(int page, int pageSize);
    List<User> searchByKeyword(String keyword);
    int count();
}
