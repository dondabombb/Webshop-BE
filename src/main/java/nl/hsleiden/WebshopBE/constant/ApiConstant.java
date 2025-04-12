package nl.hsleiden.WebshopBE.constant;

public class ApiConstant {
    public static final String apiPrefix = "/api/";
    // Product routes
    public static final String getProduct = apiPrefix + "product/{productId}";
    public static final String getAllProducts = apiPrefix + "product";

    // Cart routes
    public static final String getCart = apiPrefix + "cart";
    public static final String addProductToCart = apiPrefix + "cart/products";
    public static final String getProductFromCart = apiPrefix + "cart/products/{productId}";

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
    public static final String getAllPaymentMethods = "/api/payment-methods";
    public static final String getPaymentMethod = "/api/payment-methods/{paymentId}";
    public static final String updatePaymentMethod = "/api/payment-methods/{paymentId}";
    public static final String deletePaymentMethod = "/api/payment-methods/{paymentId}";
}
