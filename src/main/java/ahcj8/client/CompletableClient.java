package ahcj8.client;

import com.ning.http.client.Response;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface CompletableClient {
    <T> CompletableFuture<T> get(String resource, Function<Response, T> producer);

    <T> CompletableFuture<T> put(String resource, String data, Function<Response, T> producer);

    void close();
}
