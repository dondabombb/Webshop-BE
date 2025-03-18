package nl.hsleiden.WebshopBE.service;

import lombok.Getter;
import lombok.Setter;
import nl.hsleiden.WebshopBE.other.ApiResponse;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiResponseService {
    private boolean success;
    private HttpStatus status;
    private ApiResponse response;

    public ApiResponseService(boolean success, HttpStatus status, ApiResponse response) {
        this.success = success;
        this.status = status;
        this.response = response;
    }
}
