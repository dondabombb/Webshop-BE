package nl.hsleiden.WebshopBE.seeder;

import nl.hsleiden.WebshopBE.DAO.ProductDAO;
import nl.hsleiden.WebshopBE.model.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProductSeeder implements CommandLineRunner {

    private final ProductDAO productDAO;

    @Autowired
    public ProductSeeder(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }
    
    @Override
    public void run(String... args) {
        // Check if products already exist
        if (productDAO.getAllProducts().isEmpty()) {
            // Create tennis ball
            ProductModel tennisBall = new ProductModel();
            tennisBall.setId("1");
            tennisBall.setName("tennisbal");
            tennisBall.setDescription("een bal denk ik");
            tennisBall.setPrice(9.99);
            tennisBall.setImageUrl("https://www.kwd.nl/media/catalog/product/cache/2/image/515x515/9df78eab33525d08d6e5fb8d27136e95/t/e/tennisbal.jpg");
            tennisBall.setStock(100);
            productDAO.createProduct(tennisBall);

            // Create football
            ProductModel football = new ProductModel();
            football.setId("2");
            football.setName("voetbal");
            football.setDescription("een bal denk ik, of niet. daarvoor moet je eerst naar de winkel");
            football.setPrice(9.99);
            football.setImageUrl("https://www.voetbalshop.nl/media/catalog/product/cache/1a662e74f62b3db31cb2d94e98c7cd90/2/4/244317_adidas-ek-2024-fussballliebe-league-voetbal-maat-5-wit-zwart-multicolor_1.jpg");
            football.setStock(100);
            productDAO.createProduct(football);

            System.out.println("Products seeded successfully!");
        } else {
            System.out.println("Products already exist in the database.");
        }
    }
} 