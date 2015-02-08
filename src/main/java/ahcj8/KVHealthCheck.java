package ahcj8;

import com.codahale.metrics.health.HealthCheck;

public class KVHealthCheck extends HealthCheck{

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
