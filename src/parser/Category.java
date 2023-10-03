package parser;

import java.util.ArrayList;
import java.util.Collections;

public class Category {
    private String title;
    private String description;
    private ArrayList<Entry> entries;

    private record Entry(String name, String url, String description, int stars) {}

    public Category(String title, String description) {
        this.title = title;
        this.description = description;
        this.entries = new ArrayList<>();
    }

    public void addEntry(String name, String url, String description, int stars) {
        this.entries.add(new Entry(name, url, description, stars));
    }

    public void sort() {
        Collections.sort(this.entries, (x, y) -> y.stars - x.stars);
    }

    public String toString() {
        return this.title + ": " + "[" + this.entries.toString() + "]";
    }

    public int length() {
        return this.entries.size();
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public ArrayList<Entry> getEntries() {
        return this.entries;
    }
}
