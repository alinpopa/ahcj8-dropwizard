package ahcj8.client;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class AhcCompletableClient implements CompletableClient {
    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private final String url;

    public AhcCompletableClient(final String url){
        this.url = url;
    }

    @Override
    public <T> CompletableFuture<T> get(String resource, Function<Response, T> producer) {
        final CompletableFuture<T> toBeCompleted = new CompletableFuture<>();
        checkedClient().prepareGet(url + "/" + resource).execute(new AsyncCompletionHandler<Response>() {
            @Override
            public Response onCompleted(Response response) throws Exception {
                toBeCompleted.complete(producer.apply(response));
                return response;
            }

            @Override
            public void onThrowable(Throwable t){
                toBeCompleted.completeExceptionally(t);
            }
        });
        return toBeCompleted;
    }

    @Override
    public <T> CompletableFuture<T> put(String resource, String data, Function<Response, T> producer) {
        final CompletableFuture<T> toBeCompleted = new CompletableFuture<>();
        checkedClient().preparePut(url + "/" + resource).setBody(data).execute(new AsyncCompletionHandler<Response>() {
            @Override
            public Response onCompleted(Response response) throws Exception {
                toBeCompleted.complete(producer.apply(response));
                return response;
            }

            @Override
            public void onThrowable(Throwable t){
                toBeCompleted.completeExceptionally(t);
            }
        });
        return toBeCompleted;
    }

    @Override
    public void close() {
        asyncHttpClient.close();
    }

    private AsyncHttpClient checkedClient(){
        if(asyncHttpClient.isClosed()){
            throw new IllegalStateException("Client closed.");
        }
        return asyncHttpClient;
    }
}
