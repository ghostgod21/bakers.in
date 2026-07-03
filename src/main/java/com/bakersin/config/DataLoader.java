package com.bakersin.config;

import com.bakersin.model.Category;
import com.bakersin.model.Product;
import com.bakersin.repository.CategoryRepository;
import com.bakersin.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Seeds the database with categories and products on startup so the app runs
 * with zero setup. Product photos are generated color-coded placeholders
 * (via placehold.co) so the app works with no external accounts or API keys.
 * Swap {@link #img(String, String)} calls for real hosted product photography
 * (your own CDN, S3, Cloudinary, etc.) before going live.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataLoader(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            return; // already seeded
        }

        Category cakes = save(new Category("Cakes", "cakes",
                "Freshly baked layer cakes for every celebration", "🎂", "#E63969"));
        Category desserts = save(new Category("Pastries & Desserts", "desserts",
                "Handcrafted pastries, tarts and creamy indulgences", "🍰", "#FFC93C"));
        Category pizza = save(new Category("Pizza", "pizza",
                "Wood-fired pizzas loaded with fresh toppings", "🍕", "#EF476F"));
        Category burgers = save(new Category("Burgers", "burgers",
                "Juicy burgers stacked high and grilled fresh", "🍔", "#FF9F1C"));
        Category mocktails = save(new Category("Mocktails", "mocktails",
                "Refreshing, colourful sips for every mood", "🍹", "#4CC9F0"));
        Category snacks = save(new Category("Cookies & Snacks", "snacks",
                "Crunchy, buttery bites to share (or not)", "🍪", "#9B5DE5"));

        // ---------------- Cakes ----------------
        addProduct(cakes, "Chocolate Truffle Cake",
                "Rich layers of chocolate sponge soaked in syrup, blanketed in silky truffle ganache.",
                649, "per kg", 4.8, 24, true, 15, true);
        addProduct(cakes, "Red Velvet Cake",
                "Classic red velvet sponge with a tangy cream-cheese frosting between every layer.",
                699, "per kg", 4.7, 18, false, 0, true);
        addProduct(cakes, "Black Forest Cake",
                "Whipped cream, dark cherries and shaved chocolate on a cocoa sponge base.",
                599, "per kg", 4.6, 20, true, 10, true);
        addProduct(cakes, "Pineapple Delight Cake",
                "Light vanilla sponge, fresh pineapple chunks and airy whipped cream frosting.",
                549, "per kg", 4.5, 22, false, 0, true);
        addProduct(cakes, "Butterscotch Cake",
                "Caramelised butterscotch sponge crowned with crunchy praline and toffee drizzle.",
                629, "per kg", 4.6, 16, false, 0, true);
        addProduct(cakes, "Rainbow Sprinkle Cake",
                "Vanilla sponge in six colours, stacked tall and finished with a rainbow of sprinkles.",
                749, "per kg", 4.9, 12, true, 12, true);

        // ---------------- Pastries & Desserts ----------------
        addProduct(desserts, "Chocolate Éclair",
                "Choux pastry piped with vanilla custard and glazed with dark chocolate fondant.",
                99, "per piece", 4.6, 40, false, 0, true);
        addProduct(desserts, "Tiramisu Cup",
                "Espresso-soaked ladyfingers layered with mascarpone cream and cocoa dust.",
                179, "per cup", 4.8, 30, true, 10, true);
        addProduct(desserts, "Fruit Tart",
                "Buttery tart shell, vanilla pastry cream and a crown of glazed seasonal fruit.",
                149, "per piece", 4.5, 25, false, 0, true);
        addProduct(desserts, "Cream Puff",
                "Light choux pastry filled generously with fresh whipped Chantilly cream.",
                89, "per piece", 4.4, 35, false, 0, true);
        addProduct(desserts, "Chocolate Mousse",
                "Airy dark-chocolate mousse set in a glass, topped with cocoa shavings.",
                159, "per cup", 4.7, 28, true, 15, true);
        addProduct(desserts, "Baked Cheesecake",
                "Dense and creamy New York-style cheesecake on a buttery biscuit crust.",
                229, "per slice", 4.8, 18, false, 0, true);

        // ---------------- Pizza ----------------
        addProduct(pizza, "Margherita Pizza",
                "San Marzano tomato sauce, fresh mozzarella and basil on a hand-tossed base.",
                249, "9-inch", 4.6, 30, false, 0, true);
        addProduct(pizza, "Farmhouse Pizza",
                "Loaded with onions, capsicum, tomato, mushroom and sweet corn.",
                329, "9-inch", 4.5, 26, true, 10, true);
        addProduct(pizza, "Pepperoni Pizza",
                "Double pepperoni, mozzarella and a smoky tomato base — a classic favourite.",
                379, "9-inch", 4.8, 20, false, 0, false);
        addProduct(pizza, "Paneer Tikka Pizza",
                "Tandoori-spiced paneer cubes, onions and capsicum with a mint-mayo drizzle.",
                359, "9-inch", 4.7, 22, true, 12, true);
        addProduct(pizza, "BBQ Chicken Pizza",
                "Grilled chicken chunks, red onions and smoky barbecue sauce.",
                399, "9-inch", 4.7, 18, false, 0, false);
        addProduct(pizza, "Four Cheese Pizza",
                "Mozzarella, cheddar, parmesan and gorgonzola melted into one indulgent pie.",
                419, "9-inch", 4.9, 15, false, 0, true);

        // ---------------- Burgers ----------------
        addProduct(burgers, "Classic Veg Burger",
                "A crisp potato-veggie patty with lettuce, tomato and house mayo in a soft bun.",
                129, "per piece", 4.4, 40, false, 0, true);
        addProduct(burgers, "Cheese Burst Burger",
                "Veg patty with a molten cheese core, melting into every bite.",
                159, "per piece", 4.6, 32, true, 10, true);
        addProduct(burgers, "Crispy Chicken Burger",
                "Golden fried chicken fillet, lettuce and spiced mayo in a toasted bun.",
                189, "per piece", 4.7, 28, false, 0, false);
        addProduct(burgers, "Spicy Paneer Burger",
                "Peri-peri spiced paneer patty layered with fresh onions and tangy sauce.",
                169, "per piece", 4.5, 26, true, 15, true);
        addProduct(burgers, "Double Patty Burger",
                "Two juicy grilled patties, double cheese, and all the classic fixings.",
                229, "per piece", 4.8, 18, false, 0, false);
        addProduct(burgers, "Smoky BBQ Burger",
                "Grilled patty glazed in smoky barbecue sauce with crispy onions on top.",
                199, "per piece", 4.6, 20, false, 0, false);

        // ---------------- Mocktails ----------------
        addProduct(mocktails, "Virgin Mojito",
                "Fresh mint, lime and soda over ice — cool, zesty and endlessly refreshing.",
                139, "per glass", 4.7, 40, false, 0, true);
        addProduct(mocktails, "Blue Lagoon",
                "Blue curaçao syrup, lemonade and soda in a stunning ocean-blue swirl.",
                159, "per glass", 4.6, 35, true, 10, true);
        addProduct(mocktails, "Strawberry Cooler",
                "Muddled strawberries, lime and soda for a fruity, tangy refresher.",
                149, "per glass", 4.5, 30, false, 0, true);
        addProduct(mocktails, "Mango Tango",
                "Ripe mango puree shaken with orange juice and a splash of lime.",
                169, "per glass", 4.8, 28, true, 12, true);
        addProduct(mocktails, "Watermelon Splash",
                "Fresh watermelon juice with mint and a hint of black salt.",
                139, "per glass", 4.6, 32, false, 0, true);
        addProduct(mocktails, "Pina Colada Mocktail",
                "Creamy coconut milk blended with pineapple juice, served chilled.",
                179, "per glass", 4.7, 24, false, 0, true);

        // ---------------- Cookies & Snacks ----------------
        addProduct(snacks, "Choco Chip Cookies",
                "Crisp on the edges, chewy in the middle, loaded with chocolate chips.",
                199, "per box (6)", 4.7, 45, false, 0, true);
        addProduct(snacks, "Butter Cookies",
                "Melt-in-the-mouth eggless butter cookies with a rich, buttery aroma.",
                179, "per box (6)", 4.5, 40, false, 0, true);
        addProduct(snacks, "Garlic Breadsticks",
                "Oven-baked breadsticks brushed with garlic butter and herbs.",
                129, "per box (4)", 4.4, 35, true, 10, true);
        addProduct(snacks, "Cheese Nachos",
                "Crunchy nacho chips smothered in warm cheese sauce and jalapeños.",
                159, "per box", 4.6, 30, false, 0, true);
        addProduct(snacks, "Brownie Bites",
                "Fudgy chocolate brownie bites with a crackly top and gooey centre.",
                149, "per box (6)", 4.8, 38, true, 15, true);
        addProduct(snacks, "Red Velvet Cookies",
                "Soft red velvet cookies swirled with white chocolate chunks.",
                189, "per box (6)", 4.6, 26, false, 0, true);

        System.out.println("✅ Bakers.in seed data loaded: "
                + categoryRepository.count() + " categories, "
                + productRepository.count() + " products.");
    }

    private Category save(Category category) {
        return categoryRepository.save(category);
    }

    private void addProduct(Category category, String name, String description, double price,
                             String unit, double rating, int stock, boolean onSale,
                             int discountPercent, boolean veg) {
        Product product = new Product();
        product.setCategory(category);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(BigDecimal.valueOf(price));
        product.setUnit(unit);
        product.setRating(rating);
        product.setStock(stock);
        product.setOnSale(onSale);
        product.setDiscountPercent(discountPercent);
        product.setVeg(veg);
        product.setImageUrl(img(category.getAccentColor(), name));
        productRepository.save(product);
    }

    /** Generates a colour-coded placeholder image URL labelled with the product name. */
    private String img(String hexColor, String label) {
        String color = hexColor.replace("#", "");
        String encodedLabel = URLEncoder.encode(label, StandardCharsets.UTF_8).replace("+", "+");
        return "https://placehold.co/600x480/" + color + "/FFFFFF?text=" + encodedLabel + "&font=poppins";
    }
}
