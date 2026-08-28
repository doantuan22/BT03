package vn.iotstar.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String icon;
    private List<Product> products = new ArrayList<>();

    public Category() {}

    public Category(int id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    /**
     * Relationship accessor: Category hasMany Products.
     *
     * @return List of products in this category
     */
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
