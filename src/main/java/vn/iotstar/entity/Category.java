package vn.iotstar.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "category")
@NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c")
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_id")
    private int id;

    @NotEmpty(message = "Category name must not be empty")
    @Column(name = "cate_name", columnDefinition = "nvarchar(255) not null")
    private String name;

    @Column(name = "icons", columnDefinition = "nvarchar(500) null")
    private String icon;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

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
     * @return List of products belonging to this category
     */
    public List<Product> products() {
        return this.products;
    }

    public void addProduct(Product product) {
        if (product != null) {
            this.products.add(product);
            product.setCategory(this);
        }
    }

    public void removeProduct(Product product) {
        if (product != null) {
            this.products.remove(product);
            product.setCategory(null);
        }
    }
}
