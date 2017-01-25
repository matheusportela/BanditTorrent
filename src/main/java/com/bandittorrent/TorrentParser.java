package com.bandittorrent;

import java.io.File;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class TorrentParser {
    private static final Logger logger = Logger.getLogger(TorrentParser.class.getName());

    public String readTorrentFile(String path) {
        StringBuilder result = new StringBuilder();

        try {
            Scanner scanner = new Scanner(new File(path));

            while (scanner.hasNext()) {
                result.append(scanner.nextLine());
            }

            scanner.close();
        } catch (java.io.FileNotFoundException e) {
            logger.error("File %s does not exist");
        }

        return result.toString();
    }
}
