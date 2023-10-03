package parser;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;
import java.net.URI;

import java.io.IOException;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Base64;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.FieldNamingPolicy;

public class GitHub {
    private static Logger LOGGER = Logger.getLogger(GitHub.class.getName());

    private HttpClient client;
    private Gson decoder;

    private String token;

    private record Content(String content) {}
    private record Stars(int stargazersCount) {}

    private Pattern regRepo = Pattern.compile("https://github.com/(.+)");

    public GitHub(String token) {
        this.client = HttpClient.newHttpClient();
        this.decoder = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
        this.token = token;
    }

    public String getContent() {
        LOGGER.info("Fetching GitHub Content");

        var request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.github.com/repos/akullpp/awesome-java/contents/README.md"))
            .header("Content-Type", "application/json")
            .header("X-GitHub-Api-Version", "2022-11-28")
            .header("Authorization", "Bearer " + this.token)
            .build();

        try {
            var response = this.client.send(request, BodyHandlers.ofString());

            var body = this.decoder.fromJson(response.body(), Content.class);
            if (body.content == null) {
                LOGGER.severe(response.body());
            }

            var decoded = Base64.getDecoder().decode(body.content.replace("\n", ""));

            LOGGER.info("Finished fetching.");
            return new String(decoded);
        } catch(IOException|InterruptedException e) {
            LOGGER.severe(e.toString());
        }

        return "";
    }

    public int getStars(String fullPath) {
        LOGGER.fine("Fetching Starcount for " + fullPath);

        Matcher match = regRepo.matcher(fullPath);
        if(!match.find()) {
            return 0;
        }
        var name = match.group(1);

        String url = String.format("https://api.github.com/repos/%s", name);

        var request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .header("X-GitHub-Api-Version", "2022-11-28")
            .header("Authorization", "Bearer " + this.token)
            .build();

        try {
            var response = this.client.send(request, BodyHandlers.ofString());

            var body = this.decoder.fromJson(response.body(), Stars.class); // TODO: Rate limit?
            return body.stargazersCount;
        } catch(IOException|InterruptedException e) {
            LOGGER.severe(e.toString());
        }

        return 0;
    }
}
