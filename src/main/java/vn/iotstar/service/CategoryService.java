package vn.iotstar.service;

import java.util.List;
import vn.iotstar.model.Category;

public interface CategoryService {
    void insert(Category category);
    void edit(Category category);
    void delete(int id);
    Category get(int id);
    Category findByName(String name);
    List<Category> getAll();
    List<Category> getAll(int page, int pageSize);
    List<Category> searchByName(String keyword);
    int count();
}
