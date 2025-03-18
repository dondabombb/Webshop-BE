package nl.hsleiden.WebshopBE.other;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse {
    private Object result;
    private String message;

    public ApiResponse(Object result, String message) {
        this.result = result;
        this.message = message;
    }
}
