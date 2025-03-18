package nl.hsleiden.WebshopBE.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class OrderModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItemModel> items = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address_id")
    private AddressModel shippingAddress;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_address_id")
    private AddressModel billingAddress;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private String orderStatus;

    @Column(nullable = false)
    private double totalAmount;

    // Voeg een item toe aan de bestelling
    public void addItem(OrderItemModel item) {
        items.add(item);
    }
} 