package com.annakuzniak;

import java.nio.file.Files;
import java.nio.file.Path;

public class DataSetLoader {

    public String loadBook() throws Exception {

        Path filePath = Path.of("data", "tajemniczy-ogrod.txt");
        return Files.readString(filePath);
    }

}
