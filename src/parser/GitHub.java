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

    private record Stars(int stargazersCount) {}

    private Pattern regRepo = Pattern.compile("https://github.com/(.+)");

    public GitHub(String token) {
        this.client = HttpClient.newHttpClient();
        this.decoder = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
        this.token = token;
    }

    public int getStars(String fullPath) {
        LOGGER.fine("Fetching Starcount for " + fullPath);

        Matcher match = regRepo.matcher(fullPath);
        if(!match.find()) {
            return 0;
        }
        var name = match.group(1);

        String url = String.format("https://api.github.com/repos/%s", name);

        var builder = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .header("X-GitHub-Api-Version", "2022-11-28");

        if(this.token != null) {
            builder.header("Authorization", "Bearer " + this.token);
        }
        var request = builder.build();

        try {
            var response = this.client.send(request, BodyHandlers.ofString());
            var body = this.decoder.fromJson(response.body(), Stars.class);

            return body.stargazersCount;
        } catch(IOException|InterruptedException e) {
            LOGGER.severe(e.toString());
        }

        return -1;
    }
}
