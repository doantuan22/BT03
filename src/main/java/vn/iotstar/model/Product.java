package vn.iotstar.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String slug;
    private String description;
    private BigDecimal price;
    private String image;
    private int categoryId;
    private String categoryName;
    private Category category;
    private Date createdAt;
    private Date updatedAt;

    public Product() {}

    public Product(int id, String name, String slug, String description, BigDecimal price, String image, int categoryId, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.price = price;
        this.image = image;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Product(int id, String name, String slug, String description, BigDecimal price, String image, Category category, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
        if (category != null) {
            this.categoryId = category.getId();
            this.categoryName = category.getName();
        }
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) {
        this.category = category;
        if (category != null) {
            this.categoryId = category.getId();
            this.categoryName = category.getName();
        }
    }

    /**
     * Relationship accessor: Product belongsTo Category.
     *
     * @return Category this product belongs to
     */
    public Category category() {
        return this.category;
    }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
