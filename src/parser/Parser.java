package parser;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Parser {
    private Pattern regTitle = Pattern.compile("####? (.+)");
    private Pattern regDescription = Pattern.compile("_(.+)_");
    private Pattern regEntry = Pattern.compile("\\- \\[(.+)\\]\\(([\\w://\\.\\-]+)\\) \\- (.+)");

    private GitHub source;

    public Parser(GitHub source) {
        this.source = source;
    }

    public ArrayList<Category> parse(String content) {
        String[] lines = content.split("\n");

        ArrayList<Category> result = new ArrayList<>();

        // Example:
        // ### <Title of category>
        // 
        // _Description about the category_
        //
        // - [Name of first entry](http://example.com) - Description of entry
        // - [Name of second entry](http://example.com) - Another description of the entry

        // for(int i = 0; i < lines.length; i++) {
        for(int i = 0; i < lines.length; i++) {
            // Title.
            Matcher nameMatcher = regTitle.matcher(lines[i]);
            if(!nameMatcher.find()) {
                continue;
            }
            String title = nameMatcher.group(1);;

            i += 2;

            // Description.
            Matcher descrMatcher = regDescription.matcher(lines[i]);
            if(!descrMatcher.find()) {
                continue;
            }
            String description = descrMatcher.group(1);

            i += 2;

            var category = new Category(title, description);

            // Get entries.
            for(; true; i++) {
                Matcher entriesMatcher = regEntry.matcher(lines[i]);
                if(!entriesMatcher.find()) {
                    break;
                }
                String name = entriesMatcher.group(1);
                String repoUrl = entriesMatcher.group(2);
                String descr = entriesMatcher.group(3);

                if(!repoUrl.contains("github.com")) {
                    continue;
                }
                int starCount = this.source.getStars(repoUrl);

                // Some projects were renamed, forked, moved and their starCount is therefore zero.
                // -> Ignore those.
                if(starCount > 0) {
                    category.addEntry(name, repoUrl, descr, starCount);
                }

            }
            if(category.length() > 0) {
                result.add(category);
            }
        }

        // Sort by stars.
        for(Category category : result ) {
            category.sort();
        }

        return result;
    }
}
