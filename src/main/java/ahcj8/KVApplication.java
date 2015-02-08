package ahcj8;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class KVApplication extends Application<KVConfiguration> {
    public static void main(String[] args) throws Exception {
        new KVApplication().run(args);
    }

    @Override
    public String getName(){
        return "kv";
    }

    @Override
    public void initialize(Bootstrap<KVConfiguration> bootstrap){
    }

    @Override
    public void run(KVConfiguration configuration, Environment environment) throws Exception {
        final KVResource resource = new KVResource();
        final KVHealthCheck healthCheck = new KVHealthCheck();
        environment.healthChecks().register("kv", healthCheck);
        environment.jersey().register(resource);
    }
}
