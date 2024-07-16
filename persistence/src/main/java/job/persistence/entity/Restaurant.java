package job.persistence.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String address;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "restaurant_product",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products = new HashSet<>();
    private int tables;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTables() {
        return tables;
    }

    public void setTables(int tables) {
        this.tables = tables;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant restaurant)) return false;
        return name.equalsIgnoreCase(restaurant.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }
}
