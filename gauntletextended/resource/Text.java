// 
// Decompiled by Procyon v0.5.36
// 

package com.gauntletextended.resource;

import java.util.Iterator;
import org.apache.commons.text.WordUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.Collection;
import java.util.List;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import java.util.regex.Pattern;

public class Text
{
    private static final StringBuilder SB;
    private static final Pattern TAG_REGEXP;
    public static final JaroWinklerDistance DISTANCE;
    public static final Splitter COMMA_SPLITTER;
    private static final Joiner COMMA_JOINER;
    public static final CharMatcher JAGEX_PRINTABLE_CHAR_MATCHER;
    
    public static List<String> fromCSV(final String input) {
        return (List<String>)Text.COMMA_SPLITTER.splitToList((CharSequence)input);
    }
    
    public static String toCSV(final Collection<String> input) {
        return Text.COMMA_JOINER.join((Iterable)input);
    }
    
    public static String removeTags(String str, final boolean removeLevels) {
        if (removeLevels) {
            final int levelIdx = StringUtils.lastIndexOf((CharSequence)str, (CharSequence)"  (level");
            if (levelIdx >= 0) {
                str = str.substring(0, levelIdx);
            }
        }
        final int strLen = str.length();
        int open;
        int close;
        if ((open = StringUtils.indexOf((CharSequence)str, 60)) == -1 || (close = StringUtils.indexOf((CharSequence)str, 62, open)) == -1) {
            return (strLen == str.length()) ? str : str.substring(0, strLen - 1);
        }
        if (open == 0) {
            if ((open = close + 1) >= strLen) {
                return "";
            }
            if ((open = StringUtils.indexOf((CharSequence)str, 60, open)) == -1 || StringUtils.indexOf((CharSequence)str, 62, open) == -1) {
                return StringUtils.substring(str, close + 1);
            }
            open = 0;
        }
        Text.SB.setLength(0);
        int i = 0;
        while (true) {
            if (open != i) {
                Text.SB.append(str.charAt(i++));
            }
            else {
                i = close + 1;
                if ((open = StringUtils.indexOf((CharSequence)str, 60, close)) == -1 || (close = StringUtils.indexOf((CharSequence)str, 62, open)) == -1 || i >= strLen) {
                    break;
                }
                continue;
            }
        }
        while (i < strLen) {
            Text.SB.append(str.charAt(i++));
        }
        return Text.SB.toString();
    }
    
    public static String removeTags(final String str) {
        return removeTags(str, false);
    }
    
    public static String standardize(final String str, final boolean removeLevel) {
        if (StringUtils.isBlank((CharSequence)str)) {
            return str;
        }
        return removeTags(str, removeLevel).replace(' ', ' ').trim().toLowerCase();
    }
    
    public static String standardize(final String str) {
        return standardize(str, false);
    }
    
    public static String toJagexName(final String str) {
        return CharMatcher.ascii().retainFrom((CharSequence)str.replaceAll("[ _-]", " ")).trim();
    }
    
    public static String sanitizeMultilineText(final String str) {
        return removeTags(str.replaceAll("-<br>", "-").replaceAll("<br>", " ").replaceAll("[ ]+", " "));
    }
    
    public static String escapeJagex(final String str) {
        final StringBuilder out = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); ++i) {
            final char c = str.charAt(i);
            if (c == '<') {
                out.append("<lt>");
            }
            else if (c == '>') {
                out.append("<gt>");
            }
            else if (c == '\n') {
                out.append("<br>");
            }
            else if (c != '\r') {
                out.append(c);
            }
        }
        return out.toString();
    }
    
    public static String sanitize(final String name) {
        final String cleaned = name.contains("<img") ? name.substring(name.lastIndexOf(62) + 1) : name;
        return cleaned.replace(' ', ' ');
    }
    
    public static String titleCase(final Enum o) {
        final String toString = o.toString();
        if (o.name().equals(toString)) {
            return WordUtils.capitalize(toString.toLowerCase(), new char[] { '_' }).replace('_', ' ');
        }
        return toString;
    }
    
    public static boolean matchesSearchTerms(final Iterable<String> searchTerms, final Collection<String> keywords) {
        for (final String term : searchTerms) {
            final CharSequence s;
            if (keywords.stream().noneMatch(t -> t.contains(s) || Text.DISTANCE.apply((CharSequence)t, s) > 0.9)) {
                return false;
            }
        }
        return true;
    }
    
    static {
        SB = new StringBuilder(64);
        TAG_REGEXP = Pattern.compile("<[^>]*>");
        DISTANCE = new JaroWinklerDistance();
        COMMA_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();
        COMMA_JOINER = Joiner.on(",").skipNulls();
        JAGEX_PRINTABLE_CHAR_MATCHER = new JagexPrintableCharMatcher();
    }
}
