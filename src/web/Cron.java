package web;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;
import java.util.Timer; 
import java.util.TimerTask; 
import java.util.ArrayList; 

import parser.Reader;
import parser.GitHub;
import parser.Parser;
import parser.Category;

@Component
class Cron extends TimerTask {
    private final Logger LOGGER = Logger.getLogger(Cron.class.getName());
    private final int HOUR = 60 * 60 * 1000;

    private static int iteration = 0;

    private Reader reader;
    private GitHub github;
    private Parser parser;

    private ArrayList<Category> categories;

    Cron() {
        this.reader = new Reader();
        this.github = new GitHub(System.getenv("GITHUB_TOKEN"));
        this.parser = new Parser(this.github);

        new Timer().schedule(this, 0, HOUR); 

        LOGGER.info("Initialized cron job");
    }

    public void run() { 
        LOGGER.info("Started cron at iteration " + ++iteration);

        this.categories = parser.parse(this.reader.getContent());

        LOGGER.info("finished cron iteration");
    } 

    public ArrayList<Category> getCategories() {
        return this.categories;
    }
}
