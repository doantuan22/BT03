package vn.iotstar.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String imageUrl;
    private String imagePublicId;
    private Date createdAt;
    private Date updatedAt;
    private List<Product> products = new ArrayList<>();

    public Category() {}

    public Category(int id, String name, String imageUrl, String imagePublicId) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.imagePublicId = imagePublicId;
    }

    public Category(int id, String name, String imageUrl, String imagePublicId, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.imagePublicId = imagePublicId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getImagePublicId() { return imagePublicId; }
    public void setImagePublicId(String imagePublicId) { this.imagePublicId = imagePublicId; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    public List<Product> products() {
        return this.products;
    }

    public void addProduct(Product product) {
        if (product != null) {
            this.products.add(product);
            product.setCategory(this);
            product.setCategoryId(this.id);
            product.setCategoryName(this.name);
        }
    }

    public void removeProduct(Product product) {
        if (product != null) {
            this.products.remove(product);
            product.setCategory(null);
        }
    }
}
