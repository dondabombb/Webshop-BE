package nl.hsleiden.WebshopBE.security;

public class JwtProperties {
    public static final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 dagen
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
} 