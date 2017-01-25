package com.bandittorrent;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BencodeDecoder {
    private static final Logger logger = Logger.getLogger(BencodeDecoder.class.getName());
    private static final Pattern INT_PATTERN = Pattern.compile("i(-?\\d+)e");
    private static final Pattern STR_PATTERN = Pattern.compile("\\d+:(\\w+)");
    private int position;
    private String input = null;

    public void setInput(String input) {
        this.input = input;
    }

    public String getIntToken() {
        StringBuilder token = new StringBuilder();

        for (; input.charAt(position) != 'e'; position++)
            token.append(input.charAt(position));

        token.append('e');
        position++;

        return token.toString();
    }

    public int parseInt() {
        String token = getIntToken();
        Matcher matcher = INT_PATTERN.matcher(token);
        matcher.find();
        return Integer.valueOf(matcher.group(1));
    }

    public String getStrToken() {
        StringBuilder token = new StringBuilder();
        StringBuilder stringLength = new StringBuilder();

        for (; input.charAt(position) != ':'; position++) {
            stringLength.append(input.charAt(position));
            token.append(input.charAt(position));
        }

        token.append(':');
        position++;

        int length = Integer.valueOf(stringLength.toString());

        for (int i = 0; i < length; i++, position++)
            token.append(input.charAt(position));

        return token.toString();
    }

    public String parseStr() {
        String token = getStrToken();
        Matcher matcher = STR_PATTERN.matcher(token);
        matcher.find();

        if (matcher.matches())
            return matcher.group(1).toString();
        return "";
    }

    public List<Object> parseList() {
        List<Object> list = new ArrayList<Object>();

        for (position++; input.charAt(position) != 'e';) {
            list.add(parseRecursive());
        }

        position++;

        return list;
    }

    public Map<Object, Object> parseDict() {
        Map<Object, Object> dict = new HashMap<Object, Object>();
        Object key;
        Object value;

        for (position++; input.charAt(position) != 'e';) {
            key = parseRecursive();
            value = parseRecursive();
            dict.put(key, value);
        }

        position++;

        return dict;
    }

    public Object parseRecursive() {
        switch (input.charAt(position)) {
            case 'i':
                return parseInt();
            case 'l':
                return parseList();
            case 'd':
                return parseDict();
            default:
                return parseStr();
        }
    }

    public Object decode(String input) {
        this.input = input;

        if (this.input.length() == 0)
            return "";

        return parseRecursive();
    }
}
