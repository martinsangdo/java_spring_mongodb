package com.t3h.java.module3.service;

import java.util.Objects;

public class StringCombiner {
    public static Result combineStrings(String prefix, String[] words, Options options) {
        // Return null if any parameter is null
        if (prefix == null || words == null || options == null) {
            return null;
        }
        // Combine each word with the prefix
        String[] combined = new String[words.length];
        for (int i = 0; i < words.length; i++) {
            combined[i] = prefix + (options.uppercase ? words[i].toUpperCase() : words[i]);
        }
        return new Result(combined, combined.length);
    }
    // Object to hold options
    public static class Options {
        boolean uppercase;
        public Options(boolean uppercase) {
            this.uppercase = uppercase;
        }
    }
    // Object to hold result
    public static class Result {
        public String[] combinedWords;
        public int count;
        public Result(String[] combinedWords, int count) {
            this.combinedWords = combinedWords;
            this.count = count;
        }
    }
}
