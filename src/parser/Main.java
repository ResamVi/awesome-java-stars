package parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class Main {
    public static void main(String[] args) {
        var token = System.getenv("GITHUB_TOKEN");

        var source = new GitHub(token);
        var parser = new Parser(source);

        var result = parser.parse(source.getContent());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(result));
    }
}
