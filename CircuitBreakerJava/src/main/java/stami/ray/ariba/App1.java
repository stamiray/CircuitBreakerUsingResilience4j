package stami.ray.ariba;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.SupplierUtils;
import io.vavr.collection.List;
import java.time.Duration;
import java.util.function.Supplier;

public class App1 implements CBService1 {
    private String name;

    public static void main(String[] args) {
        CBService1 cbService = new App1();
        List<String> list = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine","ten","one","one","one");


        //Circuit breaker configuration
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .slidingWindow(10, 5, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .failureRateThreshold(50)
                .permittedNumberOfCallsInHalfOpenState(3)
                .waitDurationInOpenState(Duration.ofSeconds(5))
                .build();

        // Circuit Breaker Registry
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);

        //create circuit breaker
        CircuitBreaker circuitBreaker = registry.circuitBreaker("myCB");

        //supplier
        Supplier<Integer> supplier = CircuitBreaker.decorateSupplier(circuitBreaker, cbService::callService);

        //call the method
        Supplier<Integer> supplierWithResultHandling = SupplierUtils.andThen(supplier,  result -> {
            if (result == 400) {
                System.out.println("Customize 400 response");
            } else if (result == 500) {
                System.out.println("Customize 400 response");
            }
            try {
                if (cbService.getName().equalsIgnoreCase("six") || cbService.getName().equalsIgnoreCase("nine")) {
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return result;
        });

        for (String x : list) {
            cbService.setName(x);
            Integer result = circuitBreaker.executeSupplier(supplierWithResultHandling);
        }
    }

    public Integer callService() {
        System.out.println("In callService method------ name:" + getName());
        if (this.getName().equalsIgnoreCase("one") || this.getName().equalsIgnoreCase("two")) {
            return 200;
        } else if(this.getName().equalsIgnoreCase("three")){
            return 400;
        }else if(this.getName().equalsIgnoreCase("four")){
            return 500;
        }
        throw new RuntimeException();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
