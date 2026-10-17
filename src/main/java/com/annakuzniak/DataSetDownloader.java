package com.annakuzniak;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataSetDownloader {

    public void downloadBook() throws Exception {

        String bookUrl = "https://wolnelektury.pl/media/book/txt/tajemniczy-ogrod.txt";

        Path filePath = Path.of("data", "tajemniczy-ogrod.txt");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(bookUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Download failed : HTTP error code : " + response.statusCode()
            );
        }

        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, response.body());

        System.out.println("Book downloaded successfully");
        System.out.println("Saved to: " + filePath.toAbsolutePath());
    }

}
