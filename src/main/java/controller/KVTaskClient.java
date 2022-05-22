package controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;
    private final String apiKey;

    public KVTaskClient(int port) {
        url = "http://localhost:" + port + "/";
        apiKey = register(url);
    }

    private String register(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "register"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IllegalArgumentException("Зарегистрироваться не удалось, код ошибки: "
                        + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new IllegalArgumentException("Can't do save request", e);
        }
    }

    public String load(String key) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load/" + key + "?API_KEY=" + apiKey))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IllegalArgumentException("Can't do save request, status code" + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new IllegalArgumentException("Can't do save request", e);
        }
    }

    public void put(String key, String value) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(value);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save/" + key + "?API_KEY=" + apiKey))
                    .POST(body)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IllegalArgumentException("Can't do save request, status code" + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new IllegalArgumentException("Can't do save request", e);
        }
    }
}
