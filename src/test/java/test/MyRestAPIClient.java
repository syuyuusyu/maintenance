package test;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonArray;

public class MyRestAPIClient {

  private HttpClient client;

  public MyRestAPIClient(Vertx vertx) {
    // Create the HTTP client and configure the host and post.
    client = vertx.createHttpClient(new HttpClientOptions()
        .setDefaultHost("192.168.1.193")
        .setDefaultPort(8080)
    );
  }

  public void close() {
    // Don't forget to close the client when you are done.
    client.close();
  }

  public void login(Handler<AsyncResult<JsonArray>> handler) {
    // Emit a HTTP GET
    client.post("/login?id=syu&passwd=123456",
        response ->
            // Handler called when the response is received
            // We register a second handler to retrieve the body
            response.bodyHandler(body -> {
              // When the body is read, invoke the result handler
              handler.handle(Future.succeededFuture(body.toJsonArray()));
            }))
        .exceptionHandler(t -> {
          // If something bad happen, report the failure to the passed handler
          handler.handle(Future.failedFuture(t));
        })
        // Call end to send the request
      .end();
  }

  public void addName(String name, Handler<AsyncResult<Void>> handler) {
    // Emit a HTTP POST
    client.post("/names",
        response -> {
          // Check the status code and act accordingly
          if (response.statusCode() == 200) {
            handler.handle(Future.succeededFuture());
          } else {
            handler.handle(Future.failedFuture(response.statusMessage()));
          }
        })
        .exceptionHandler(t -> handler.handle(Future.failedFuture(t)))
        // Pass the name we want to add
        .end(name);
  }
}
