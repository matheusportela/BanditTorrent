package com.bandittorrent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BencodeDecoderTest {
    BencodeDecoder bencodeDecoder;

    @BeforeEach
    void setUp() {
        bencodeDecoder = new BencodeDecoder();
    }

    @Test
    void parseInt_expectPositiveNumber() {
        bencodeDecoder.setInput("i42e");
        assertEquals(42, bencodeDecoder.parseInt());
    }

    @Test
    void parseInt_expectNegativeNumber() {
        bencodeDecoder.setInput("i-42e");
        assertEquals(-42, bencodeDecoder.parseInt());
    }

    @Test
    void parseInt_expectZero() {
        bencodeDecoder.setInput("i0e");
        assertEquals(0, bencodeDecoder.parseInt());
    }

    @Test
    void parseByteString_expectValidString() {
        bencodeDecoder.setInput("5:abcde");
        assertEquals("abcde", bencodeDecoder.parseStr());
    }

    @Test
    void parseByteString_expectEmptyString() {
        bencodeDecoder.setInput("0:");
        assertEquals("", bencodeDecoder.parseStr());
    }

    @Test
    void parseList_expectEmptyList() {
        List<Object> result = new ArrayList<Object>();
        bencodeDecoder.setInput("le");
        assertEquals(result, bencodeDecoder.parseList());
    }

    @Test
    void parseList_expectString() {
        List<Object> result = new ArrayList<Object>();
        result.add("abcde");
        bencodeDecoder.setInput("l5:abcdee");
        assertEquals(result, bencodeDecoder.parseList());
    }

    @Test
    void parseList_expectStringAndInt() {
        List<Object> result = new ArrayList<Object>();
        result.add("abcde");
        result.add(42);
        bencodeDecoder.setInput("l5:abcdei42ee");
        assertEquals(result, bencodeDecoder.parseList());
    }

    @Test
    void parseList_expectThreeStringsAndTwoInts() {
        List<Object> result = new ArrayList<Object>();
        result.add("abcde");
        result.add("fghijklmn");
        result.add(-42);
        result.add("");
        result.add(0);
        bencodeDecoder.setInput("l5:abcde9:fghijklmni-42e0:i0ee");
        assertEquals(result, bencodeDecoder.parseList());
    }

    @Test
    void parseDictionary_expectEmptyDictionary() {
        Map<String, Object> result = new HashMap<String, Object>();
        bencodeDecoder.setInput("de");
        assertEquals(result, bencodeDecoder.parseDict());
    }

    @Test
    void parseDictionary_expectString() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("abcde", "fghij");
        bencodeDecoder.setInput("d5:abcde5:fghije");
        assertEquals(result, bencodeDecoder.parseDict());
    }

    @Test
    void parseDictionary_expectStringAndInt() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("abcde", "fghij");
        result.put("answer_to_universe", 42);
        bencodeDecoder.setInput("d5:abcde5:fghij18:answer_to_universei42ee");
        assertEquals(result, bencodeDecoder.parseDict());
    }

    @Test
    void decode_expectEmpty() {
        String result = "";
        assertEquals(result, bencodeDecoder.decode(""));
    }

    @Test
    void decode_expectString() {
        String result = "abcde";
        assertEquals(result, bencodeDecoder.decode("5:abcde"));
    }

    @Test
    void decode_expectStringIntListAndDict() {
        List<Object> result = new ArrayList<Object>();
        result.add("abcde");
        result.add(42);

        List<Object> list = new ArrayList<Object>();
        list.add("defgh");
        list.add(-42);
        result.add(list);

        Map<String, Object> dict = new HashMap<String, Object>();
        dict.put("answer", 42);
        result.add(dict);

        assertEquals(result, bencodeDecoder.decode("l5:abcdei42el5:defghi-42eed6:answeri42eee"));
    }

    @Test
    void decode_expectListWithInnerDict() {
        List<Object> result = new ArrayList<Object>();
        Map<String, Object> dict = new HashMap<String, Object>();

        dict.put("answer", 42);
        result.add(dict);

        assertEquals(result, bencodeDecoder.decode("ld6:answeri42eee"));
    }

    @Test
    void decode_expectDictWithInnerList() {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Object> list = new ArrayList<Object>();

        list.add(42);
        list.add(-42);
        list.add(0);
        result.put("answer", list);

        assertEquals(result, bencodeDecoder.decode("d6:answerli42ei-42ei0eee"));
    }
}