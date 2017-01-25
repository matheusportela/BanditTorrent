package com.bandittorrent;

public class Main {

    public static void main(String[] args) {
        TorrentParser torrentParser = new TorrentParser();
        BencodeDecoder bencodeDecoder = new BencodeDecoder();
        String data = torrentParser.readTorrentFile("midori_no_hibi.torrent");
        Object contents = bencodeDecoder.decode(data);
        System.out.println(contents);
    }
}
