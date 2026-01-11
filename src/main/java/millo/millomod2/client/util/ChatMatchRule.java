package millo.millomod2.client.util;

import java.util.regex.Pattern;

public class ChatMatchRule {

    // Direct matches
    private String[] matches;

    // Regex patterns
    private Pattern[] patterns;


    public ChatMatchRule(String... matches) {
        this.matches = matches;
        this.patterns = new Pattern[0];
    }

    public ChatMatchRule(Pattern... patterns) {
        this.matches = new String[0];
        this.patterns = patterns;
    }

    public ChatMatchRule addMatches(String... matches) {
        String[] newMatches = new String[this.matches.length + matches.length];
        System.arraycopy(this.matches, 0, newMatches, 0, this.matches.length);
        System.arraycopy(matches, 0, newMatches, this.matches.length, matches.length);
        this.matches = newMatches;
        return this;
    }

    public ChatMatchRule addPatterns(Pattern... patterns) {
        Pattern[] newPatterns = new Pattern[this.patterns.length + patterns.length];
        System.arraycopy(this.patterns, 0, newPatterns, 0, this.patterns.length);
        System.arraycopy(patterns, 0, newPatterns, this.patterns.length, patterns.length);
        this.patterns = newPatterns;
        return this;
    }

    public boolean matches(String message) {
        for (String match : matches) {
            if (message.equalsIgnoreCase(match)) {
                return true;
            }
        }
        for (Pattern pattern : patterns) {
            if (pattern.matcher(message).find()) {
                return true;
            }
        }
        return false;
    }
}
