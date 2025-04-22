package nl.hsleiden.WebshopBE.constant;

public class ApiConstant {
    public static final String apiPrefix = "/api/";
    // Product routes
    public static final String getProduct = apiPrefix + "product/{productId}";
    public static final String getAllProducts = apiPrefix + "product";

    // Cart routes
    public static final String getCart = apiPrefix + "cart";
    public static final String addProductToCart = apiPrefix + "cart/products";
    public static final String removeProductFromCart = apiPrefix + "cart/products/{cartId}/{productId}";
    public static final String clearCart = apiPrefix + "cart/clear";
    public static final String incrementProduct = apiPrefix + "cart/product/increment";
    public static final String decrementProduct = apiPrefix + "cart/product/decrement";

    // Users routes
    public static final String getUser = apiPrefix + "user/{userId}";
    public static final String getAllUsers = apiPrefix + "user";
    public static final String register = apiPrefix + "auth/register";
    public static final String login = apiPrefix + "auth/login";
    public static final String addShippingAddressToUser = apiPrefix + "user/{userId}/shipping-address";
    public static final String addBillingAddressToUser = apiPrefix + "user/{userId}/billing-address";

    // Order routes
    public static final String createOrder = apiPrefix + "order";
    public static final String getOrder = apiPrefix + "order/{orderId}";
    public static final String getAllOrders = apiPrefix + "order";
    public static final String getUserOrders = apiPrefix + "user/orders";
    public static final String updateOrderStatus = apiPrefix + "order/{orderId}/status";

    // Payment methods routes
    public static final String getAllPaymentMethods = apiPrefix + "payment-methods";
    public static final String getPaymentMethod = apiPrefix + "payment-methods/{paymentId}";

    // Category methods routes
    public static final String getAllCategories = apiPrefix + "categories";
    public static final String getCategory = apiPrefix + "categories/{categoryId}";
}
