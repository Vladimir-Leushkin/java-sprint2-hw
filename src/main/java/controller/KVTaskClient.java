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
        } catch (IOException | java.lang.InterruptedException InterruptedException) {
            System.out.println("Во время выполнения запроса возникла ошибка. " +
                    "Проверьте, пожалуйста, URL-адрес и повторите попытку.");

        } catch (IllegalArgumentException e) {
            System.out.println("Введённый вами адрес не соответствует формату URL. " +
                    "Попробуйте, пожалуйста, снова.");
        }
        return url;
    }

    public String load(String key) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiKey))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //System.out.println(response.body());
            if (response.statusCode() != 200) {
                throw new IllegalArgumentException("Не удалось загрузить ответ KVServer, код ошибки " +
                        " " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new IllegalArgumentException("Не удалось загрузить ответ KVServer", e);
        }
    }

    public void put(String key, String value) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(value);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiKey))
                    .POST(body)
                    .build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() != 200) {
                throw new IllegalArgumentException("Не удалось сохранить запрос, код ошибки" + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new IllegalArgumentException("Не удалось сохранить запрос", e);
        }
    }
}
