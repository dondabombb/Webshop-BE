package nl.hsleiden.WebshopBE.other;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;


public class ApiResponse {
    @Setter
    private Object result;
    @Setter
    private String message;
    private Map<String, Object> extraFields = new HashMap<>();

    public ApiResponse(){};

    public ApiResponse(Object result, String message) {
        this.result = result;
        this.message = message;
    }

    public Map<String, Object> getContent(){
        Map<String, Object> response = new HashMap<>();

        if(this.result != null){
            response.put("result", this.result);
        }
        if(this.message != null) {
            response.put("message", this.message);
        }

        response.putAll(this.extraFields);
        return response;
    }

    public void addField(String name, Object content){
        this.extraFields.put(name, content);
    }

}
