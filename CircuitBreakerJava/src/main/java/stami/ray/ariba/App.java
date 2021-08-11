package stami.ray.ariba;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.collection.List;
import io.vavr.control.Try;

import java.time.Duration;
import java.util.function.Supplier;

public class App implements CBService {
    private String name;

    public static void main(String[] args) {
        CBService cbService = new App();
        List<String> list = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine","ten","eleven","twelve","one","one","one");


        //Circuit breaker configuration
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .slidingWindow(10, 5, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .failureRateThreshold(50)
                .permittedNumberOfCallsInHalfOpenState(2)
                .waitDurationInOpenState(Duration.ofSeconds(5))
                .build();

        // Circuit Breaker Registry
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);

        //create circuit breaker
        CircuitBreaker circuitBreaker = registry.circuitBreaker("myCB");

        //supplier
        Supplier<Integer> supplier = CircuitBreaker.decorateSupplier(circuitBreaker, cbService::callService);

        //call the method
        for (String x : list) {
            cbService.setName(x);
            Try<Integer> result = Try.ofSupplier(supplier).recover(cbService::fallBack);
            System.out.println(result);
        }
    }

    public Integer callService() {
        System.out.println("In callService method------ name:" + getName());
        if (this.getName().equalsIgnoreCase("one") || this.getName().equalsIgnoreCase("two")) {
            return 200;
        } else {
           throw new RuntimeException();
        }
    }

    public Integer fallBack(Throwable T) {
        System.out.println("In fallBack method");
        try {
            if (this.getName().equalsIgnoreCase("six") || this.getName().equalsIgnoreCase("nine")||this.getName().equalsIgnoreCase("twelve")) {
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 404;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
