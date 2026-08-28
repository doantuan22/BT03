package vn.iotstar.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
@NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Product name must not be empty")
    @Column(name = "name", columnDefinition = "nvarchar(255) not null")
    private String name;

    @NotEmpty(message = "Product slug must not be empty")
    @Column(name = "slug", columnDefinition = "nvarchar(255) not null")
    private String slug;

    @Column(name = "description", columnDefinition = "nvarchar(max) null")
    private String description;

    @Column(name = "price", precision = 18, scale = 2, nullable = false)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "image", columnDefinition = "nvarchar(500) null")
    private String image;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "cate_id", nullable = false)
    private Category category;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = new Date();
        }
        if (updatedAt == null) {
            updatedAt = new Date();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
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
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

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
