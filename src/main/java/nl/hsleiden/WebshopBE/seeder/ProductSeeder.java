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
            tennisBall.setName("Tennis Ball");
            tennisBall.setDescription("High-quality tennis ball for professional and recreational play");
            tennisBall.setPrice(12.99);
            tennisBall.setImageUrl("https://www.kwd.nl/media/catalog/product/cache/2/image/515x515/9df78eab33525d08d6e5fb8d27136e95/t/e/tennisbal.jpg");
            tennisBall.setStock(100);
            productDAO.createProduct(tennisBall);

            // Create football
   
          ProductModel football = new ProductModel();         football.setName("Football");
            football.setDescription("Official size soccer ball with durable construction for field play");
            football.setPrice(24.99);
            football.setImageUrl("https://www.voetbalshop.nl/media/catalog/product/cache/1a662e74f62b3db31cb2d94e98c7cd90/2/4/244317_adidas-ek-2024-fussballliebe-league-voetbal-maat-5-wit-zwart-multicolor_1.jpg");
            football.setStock(100);
            productDAO.createProduct(football);

            // Create rugby ball
            ProductModel rugbyBall = new ProductModel();
            rugbyBall.setName("Rugby Ball");
            rugbyBall.setDescription("Professional rugby ball with optimal grip and durability");
            rugbyBall.setPrice(29.99);
            rugbyBall.setImageUrl("https://www.partywinkel.nl/cdn/shop/files/file_348fc144-ccaf-4aa6-9836-e54ab1805a9b.jpg?v=1718874513");
            rugbyBall.setStock(75);
            productDAO.createProduct(rugbyBall);

            // Create football shoes
            ProductModel footballShoes = new ProductModel();
            footballShoes.setName("Football Shoes");
            footballShoes.setDescription("Professional soccer cleats with superior traction and comfort");
            footballShoes.setPrice(89.99);
            footballShoes.setImageUrl("https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/8131fe5c-6f74-4cc4-97ce-1cf79434db61/ZM+VAPOR+16+ELITE+FG+LV8.png");
            footballShoes.setStock(50);
            productDAO.createProduct(footballShoes);

            // Create tennis racket
            ProductModel tennisRacket = new ProductModel();
            tennisRacket.setName("Tennis Racket");
            tennisRacket.setDescription("Lightweight tennis racket with balanced power and control");
            tennisRacket.setPrice(79.99);
            tennisRacket.setImageUrl("https://www.tennispro.nl/media/catalog/product/cache/8/thumbnail/1200x/9df78eab33525d08d6e5fb8d27136e95/w/r/wr074011u_1.jpg");
            tennisRacket.setStock(40);
            productDAO.createProduct(tennisRacket);

            // Create baseball
            ProductModel baseball = new ProductModel();
            baseball.setName("Baseball");
            baseball.setDescription("Regulation baseball with leather cover and cork center");
            baseball.setPrice(15.99);
            baseball.setImageUrl("https://upload.wikimedia.org/wikipedia/en/1/1e/Baseball_%28crop%29.jpg");
            baseball.setStock(120);
            productDAO.createProduct(baseball);

            // Create baseball bat
            ProductModel baseballBat = new ProductModel();
            baseballBat.setName("Baseball Bat");
            baseballBat.setDescription("Aluminum baseball bat with balanced weight distribution");
            baseballBat.setPrice(59.99);
            baseballBat.setImageUrl("https://cdn11.bigcommerce.com/s-6cxk647km8/images/stencil/1280w/products/127/1302/R141_Website__56565.1606253853.jpg?c=2");
            baseballBat.setStock(35);
            productDAO.createProduct(baseballBat);

            // Create football gloves
            ProductModel footballGloves = new ProductModel();
            footballGloves.setName("Football Gloves");
            footballGloves.setDescription("Receiver gloves with enhanced grip and finger protection");
            footballGloves.setPrice(34.99);
            footballGloves.setImageUrl("https://www.epsports.co.uk/media/iopt/catalog/product/cache/414e06c0bc81c2bd58ee288e7eb43d40/v/a/vapor_jet_8_royal_415.webp");
            footballGloves.setStock(60);
            productDAO.createProduct(footballGloves);

            // Create tennis shoes
            ProductModel tennisShoes = new ProductModel();
            tennisShoes.setName("Tennis Shoes");
            tennisShoes.setDescription("Court-specific tennis shoes with lateral support and cushioning");
            tennisShoes.setPrice(94.99);
            tennisShoes.setImageUrl("https://media.babolat.com/image/upload/f_auto,q_auto,c_pad,w_3024,h_3024/v1719837023/Product_Media/2025/Tennis/Shoes/SFX/3A0S25A529-SFX_4_ALL_COURT_MEN-1005-1-Exterieur.png");
            tennisShoes.setStock(45);
            productDAO.createProduct(tennisShoes);

            // Create baseball helmet
            ProductModel baseballHelmet = new ProductModel();
            baseballHelmet.setName("Baseball Helmet");
            baseballHelmet.setDescription("Protective batting helmet with impact-resistant shell");
            baseballHelmet.setPrice(49.99);
            baseballHelmet.setImageUrl("https://m.media-amazon.com/images/I/712tex5nyZL._AC_SL1500_.jpg");
            baseballHelmet.setStock(30);
            productDAO.createProduct(baseballHelmet);

            System.out.println("Products seeded successfully!");
        } else {
            System.out.println("Products already exist in the database.");
        }
    }
}