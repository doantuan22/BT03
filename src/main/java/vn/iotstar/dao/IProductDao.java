package vn.iotstar.dao;

import java.util.List;
import vn.iotstar.entity.Product;

public interface IProductDao {
    void insert(Product product);
    void update(Product product);
    void delete(int id) throws Exception;
    Product findById(int id);
    Product findBySlug(String slug);
    List<Product> findByCategoryId(int categoryId);
    List<Product> findAll();
    List<Product> findAll(int page, int pageSize);
    List<Product> findTop10Newest();
    List<Product> findLatest(int limit);
    List<Product> searchByName(String keyword);
    List<Product> findPaginated(int page, int pageSize, Integer categoryId, String keyword, String sortBy);
    int count();
    int countFiltered(Integer categoryId, String keyword);
}
