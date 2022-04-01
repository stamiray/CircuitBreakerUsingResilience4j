package stami.sap.circuitBreakerUsingSpringBoot;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class CBController {

    @Autowired
    private RestTemplate rest;

    private static final String DEMO_SERVICE = "demo_service";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * End point calling the other one should implement circuitBreaker
     */
    @RequestMapping("/getData")
    @CircuitBreaker(name=DEMO_SERVICE, fallbackMethod = "orderFallback")
    public ResponseEntity<String> sendData() {
        String response = rest.getForObject("http://localhost:8082/data", String.class);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    /**
    a fallback method should be placed in the same class and must have the same method
     signature with just ONE extra target exception parameter
     */
    public ResponseEntity<String> orderFallback(Exception e){
        return new ResponseEntity<String>("service is down", HttpStatus.OK);

    }
}
