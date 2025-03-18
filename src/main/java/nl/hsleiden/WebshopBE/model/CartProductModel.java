package nl.hsleiden.WebshopBE.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "cart_products")
@Getter
@Setter
@NoArgsConstructor
public class CartProductModel {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartModel cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductModel product;

    @Column(nullable = false)
    private int quantity;

    public CartProductModel(CartModel cart, ProductModel product, int quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }
}
