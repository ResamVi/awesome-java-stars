package parser;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Reader {
    public String getContent() {
        InputStream stream = Reader.class.getResourceAsStream("/LIST.md");
        return readFromInputStream(stream);
    }

    private String readFromInputStream(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
}
}
