package advisor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import static advisor.EndPoints.*;
import static advisor.SecureData.*;

public class CodeReceiver {
    private static final String SUCCESS_ANSWER = "Got the code. Return back to your program.";
    private static final String ERROR_ANSWER = "Not found authorization code. Try again.";

    private final HttpServer server;
    private String code = "";
    private boolean stop = false;

    public CodeReceiver() throws IOException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);
        server.createContext("/",
                new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        String answer;
                        try {
                            String query = exchange.getRequestURI().getQuery();
                            if (query == null || query.isEmpty()) {
                                answer = ERROR_ANSWER;
                            } else {
                                String[] queryParts = query.split("=");
                                if ("code".equals(queryParts[0])) {
                                    code = queryParts[1];
                                    answer = SUCCESS_ANSWER;
                                } else {
                                    answer = ERROR_ANSWER;
                                }
                            }
                        } catch (Exception e) {
                            answer = ERROR_ANSWER;
                        }
                        exchange.sendResponseHeaders(200, answer.length());
                        exchange.getResponseBody().write(answer.getBytes());
                        exchange.getResponseBody().close();
                        stop = true;
                    }
                });
    }

    public void run() throws InterruptedException {
        server.start();

        System.out.println("use this link to request the access code:");
        System.out.println(String.format(AUTH_URL, CLIENT_ID));
        System.out.println("waiting for code...");

        while (!stop) {
            Thread.sleep(10);
        }

        if (isSuccess()) {
            System.out.println("code received");
        }

        server.stop(1);
    }

    public boolean isSuccess() {
        return !code.isEmpty();
    }

    public String getCode() {
        return code;
    }
}
