package nl.hsleiden.WebshopBE.service;

import lombok.Getter;
import lombok.Setter;
import nl.hsleiden.WebshopBE.other.ApiResponse;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
public class ApiResponseService {
    private boolean success;
    private HttpStatus status;
    private final Map<String, Object> payload;

    public ApiResponseService(boolean success, HttpStatus status, ApiResponse payload) {
        this.success = success;
        this.status = status;
        this.payload = payload.getContent();
    }
}
