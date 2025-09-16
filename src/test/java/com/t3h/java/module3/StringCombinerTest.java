package com.t3h.java.module3;

import org.junit.jupiter.api.Test;

import com.t3h.java.module3.service.StringCombiner;

import static org.junit.jupiter.api.Assertions.*;

public class StringCombinerTest {

    @Test
    public void testNullParameters() {
        assertNull(StringCombiner.combineStrings(null, null, null));
    }

    @Test
    public void testEmptyArray() {
        StringCombiner.Options options = new StringCombiner.Options(true);
        StringCombiner.Result result = StringCombiner.combineStrings("pre-", new String[0], options);

        assertNotNull(result);
        assertEquals(0, result.count);
        assertArrayEquals(new String[0], result.combinedWords);
    }

    @Test
    public void testNormalCombine() {
        StringCombiner.Options options = new StringCombiner.Options(true);
        String[] words = {"apple", "banana"};
        StringCombiner.Result result = StringCombiner.combineStrings("pre-", words, options);

        assertEquals(2, result.count);
        assertArrayEquals(new String[]{"pre-APPLE", "pre-BANANA"}, result.combinedWords);
    }

    @Test
    public void testWithoutUppercase() {
        StringCombiner.Options options = new StringCombiner.Options(false);
        String[] words = {"apple", "banana"};
        StringCombiner.Result result = StringCombiner.combineStrings("pre-", words, options);

        assertArrayEquals(new String[]{"pre-apple", "pre-banana"}, result.combinedWords);
    }
}
