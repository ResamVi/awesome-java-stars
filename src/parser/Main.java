package parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class Main {
    public static void main(String[] args) {
        var reader = new Reader();
        var source = new GitHub(System.getenv("GITHUB_TOKEN"));
        var parser = new Parser(source);
        var gson   = new GsonBuilder().setPrettyPrinting().create();

        var result = parser.parse(reader.getContent());

        System.out.println(gson.toJson(result));
    }
}
