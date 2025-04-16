package nl.hsleiden.WebshopBE.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
public class CartModel {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @JsonManagedReference
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartProductModel> products = new ArrayList<>();

    public void addProduct(CartProductModel cartProduct) {
        products.add(cartProduct);
        cartProduct.setCart(this);
    }

    public void removeProduct(CartProductModel cartProduct) {
        products.remove(cartProduct);
        cartProduct.setCart(null);
    }

    public void removeAllProducts() {
        products.forEach(product -> product.setCart(null));
        products.clear();
    }

    public double getTotalPrice() {
        return products.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
}
