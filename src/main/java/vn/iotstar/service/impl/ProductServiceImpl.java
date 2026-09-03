package vn.iotstar.service.impl;

import java.util.ArrayList;
import java.util.List;
import vn.iotstar.dao.CategoryDao;
import vn.iotstar.dao.ICategoryDao;
import vn.iotstar.dao.IProductDao;
import vn.iotstar.dao.ProductDao;
import vn.iotstar.entity.Product;
import vn.iotstar.service.IProductService;

public class ProductServiceImpl implements IProductService {

    private final IProductDao productDao = new ProductDao();
    private final ICategoryDao categoryDao = new CategoryDao();

    @Override
    public void insert(vn.iotstar.model.Product product) {
        Product entity = toEntity(product);
        productDao.insert(entity);
        product.setId(entity.getId());
    }

    @Override
    public void update(vn.iotstar.model.Product product) {
        Product entity = toEntity(product);
        productDao.update(entity);
    }

    @Override
    public void delete(int id) throws Exception {
        productDao.delete(id);
    }

    @Override
    public vn.iotstar.model.Product findById(int id) {
        Product entity = productDao.findById(id);
        return entity != null ? toModel(entity) : null;
    }

    @Override
    public vn.iotstar.model.Product findBySlug(String slug) {
        Product entity = productDao.findBySlug(slug);
        return entity != null ? toModel(entity) : null;
    }

    @Override
    public List<vn.iotstar.model.Product> findByCategoryId(int categoryId) {
        List<Product> entities = productDao.findByCategoryId(categoryId);
        return toModelList(entities);
    }

    @Override
    public List<vn.iotstar.model.Product> findAll() {
        List<Product> entities = productDao.findAll();
        return toModelList(entities);
    }

    @Override
    public List<vn.iotstar.model.Product> findAll(int page, int pageSize) {
        List<Product> entities = productDao.findAll(page, pageSize);
        return toModelList(entities);
    }

    @Override
    public List<vn.iotstar.model.Product> findTop10Newest() {
        return findLatest(10);
    }

    @Override
    public List<vn.iotstar.model.Product> findLatest(int limit) {
        List<Product> entities = productDao.findLatest(limit);
        return toModelList(entities);
    }

    @Override
    public List<vn.iotstar.model.Product> searchByName(String keyword) {
        List<Product> entities = productDao.searchByName(keyword);
        return toModelList(entities);
    }

    @Override
    public List<vn.iotstar.model.Product> findPaginated(int page, int pageSize, Integer categoryId, String keyword, String sortBy) {
        List<Product> entities = productDao.findPaginated(page, pageSize, categoryId, keyword, sortBy);
        return toModelList(entities);
    }

    @Override
    public int count() {
        return productDao.count();
    }

    @Override
    public int countFiltered(Integer categoryId, String keyword) {
        return productDao.countFiltered(categoryId, keyword);
    }

    private Product toEntity(vn.iotstar.model.Product model) {
        if (model == null) return null;
        Product entity = new Product();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setSlug(model.getSlug());
        entity.setDescription(model.getDescription());
        entity.setPrice(model.getPrice());
        entity.setImageUrl(model.getImageUrl());
        entity.setImagePublicId(model.getImagePublicId());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());

        int catId = model.getCategoryId();
        if (catId <= 0 && model.getCategory() != null) {
            catId = model.getCategory().getId();
        }
        if (catId > 0) {
            vn.iotstar.entity.Category catEntity = categoryDao.findById(catId);
            entity.setCategory(catEntity);
        }
        return entity;
    }

    private vn.iotstar.model.Product toModel(Product entity) {
        if (entity == null) return null;
        vn.iotstar.model.Product model = new vn.iotstar.model.Product();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setSlug(entity.getSlug());
        model.setDescription(entity.getDescription());
        model.setPrice(entity.getPrice());
        model.setImageUrl(entity.getImageUrl());
        model.setImagePublicId(entity.getImagePublicId());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getCategory() != null) {
            model.setCategoryId(entity.getCategory().getId());
            model.setCategoryName(entity.getCategory().getName());
            vn.iotstar.model.Category catModel = new vn.iotstar.model.Category(
                    entity.getCategory().getId(),
                    entity.getCategory().getName(),
                    entity.getCategory().getImageUrl(),
                    entity.getCategory().getImagePublicId()
            );
            model.setCategory(catModel);
        }
        return model;
    }

    private List<vn.iotstar.model.Product> toModelList(List<Product> entities) {
        List<vn.iotstar.model.Product> models = new ArrayList<>();
        if (entities != null) {
            for (Product p : entities) {
                models.add(toModel(p));
            }
        }
        return models;
    }
}
