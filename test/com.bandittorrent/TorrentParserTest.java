package com.bandittorrent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class TorrentParserTest {
    TorrentParser torrentParser;
    static final String TORRENT_FILE = "midori_no_hibi.torrent";

    @BeforeEach
    void setUp() {
        torrentParser = new TorrentParser();
    }

    @Test
    void readTorrentFile_dataGreaterThanZero() {
        String data = torrentParser.readTorrentFile(TORRENT_FILE);
        assertTrue(data.length() > 0);
    }

}