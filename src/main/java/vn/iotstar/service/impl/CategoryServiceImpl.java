package vn.iotstar.service.impl;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import vn.iotstar.dao.CategoryDao;
import vn.iotstar.dao.ICategoryDao;
import vn.iotstar.model.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.util.Constant;

public class CategoryServiceImpl implements CategoryService {
    private final ICategoryDao categoryDao = new CategoryDao();
    public void insert(Category category) {
        if (categoryDao.findByName(category.getName()) != null) throw new IllegalArgumentException("Category name already exists");
        vn.iotstar.entity.Category entity = toEntity(category);
        categoryDao.insert(entity);
        category.setId(entity.getId());
    }
    public void edit(Category category) {
        vn.iotstar.entity.Category oldCategory = categoryDao.findById(category.getId());
        if (oldCategory == null) throw new IllegalArgumentException("Category does not exist");
        vn.iotstar.entity.Category duplicate = categoryDao.findByName(category.getName());
        if (duplicate != null && duplicate.getId() != category.getId()) throw new IllegalArgumentException("Category name already exists");
        oldCategory.setName(category.getName());
        if (category.getIcon() != null) { deleteImage(oldCategory.getIcon()); oldCategory.setIcon(category.getIcon()); }
        categoryDao.update(oldCategory);
    }
    public void delete(int id) {
        vn.iotstar.entity.Category category = categoryDao.findById(id);
        if (category == null) return;
        try { categoryDao.delete(id); deleteImage(category.getIcon()); }
        catch (Exception e) { throw new IllegalStateException("Cannot delete category", e); }
    }
    public Category get(int id) { return toModel(categoryDao.findById(id)); }
    public Category findByName(String name) { return toModel(categoryDao.findByName(name)); }
    public List<Category> getAll() { return categoryDao.findAll().stream().map(this::toModel).collect(Collectors.toList()); }
    public List<Category> getAll(int page, int pageSize) { return categoryDao.findAll(page, pageSize).stream().map(this::toModel).collect(Collectors.toList()); }
    public List<Category> searchByName(String keyword) { return categoryDao.searchByName(keyword).stream().map(this::toModel).collect(Collectors.toList()); }
    public int count() { return categoryDao.count(); }
    private void deleteImage(String fileName) { if (fileName != null && !fileName.isBlank()) new File(Constant.DIR, fileName).delete(); }
    private Category toModel(vn.iotstar.entity.Category entity) {
        if (entity == null) return null;
        Category model = new Category();
        model.setId(entity.getId()); model.setName(entity.getName()); model.setIcon(entity.getIcon());
        return model;
    }
    private vn.iotstar.entity.Category toEntity(Category model) {
        vn.iotstar.entity.Category entity = new vn.iotstar.entity.Category();
        entity.setId(model.getId()); entity.setName(model.getName()); entity.setIcon(model.getIcon());
        return entity;
    }
}
