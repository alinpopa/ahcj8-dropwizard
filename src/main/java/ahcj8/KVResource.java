package ahcj8;

import ahcj8.client.AhcCompletableClient;
import ahcj8.client.CompletableClient;
import com.ning.http.client.Response;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Path("/kv")
@Produces(MediaType.APPLICATION_JSON)
public class KVResource {
    private final CompletableClient client = new AhcCompletableClient("http://0.0.0.0:9991");

    @GET
    public void sayHello(@Suspended AsyncResponse asyncResponse){
        asyncResponse.setTimeout(3, TimeUnit.SECONDS);
        final Function<Response, String> responseParser = response -> {
            try {
                return response.getResponseBody();
            } catch (Exception e) {
                return e.getMessage();
            }
        };
        final CompletableFuture<String> key = client.get("/", responseParser);
        final CompletableFuture<String> setValue = key.thenCompose(k ->
                        client.put("/kv/" + k, "SOME DATA", r -> k)
        );
        final CompletableFuture<String> value = setValue.thenCompose(k ->
                        client.get("/kv/" + k, responseParser)
        );
        value.thenApply(asyncResponse::resume);
    }
}
