package nl.hsleiden.WebshopBE.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
public class OrderItemModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductModel product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price; // Prijs op moment van bestellen

    @Column(nullable = false)
    private String productName; // Naam op moment van bestellen
} 