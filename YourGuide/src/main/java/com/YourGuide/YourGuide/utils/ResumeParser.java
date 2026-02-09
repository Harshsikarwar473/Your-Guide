package com.YourGuide.YourGuide.utils;


import java.util.regex.*;

public class ResumeParser {
    public static double extractExperienceYears(String text) {
        text = text.toLowerCase();
        Pattern p = Pattern.compile("(\\d+)(\\+)?\\s*(years|year|yrs|yr)");
        Matcher m = p.matcher(text);

        int maxYears = 0;
        while (m.find()) {
            int yrs = Integer.parseInt(m.group(1));
            if (yrs > maxYears) maxYears = yrs;
        }
        return maxYears;
    }

    public static String extractEducation(String text) {
        text = text.toLowerCase();

        if (text.contains("phd") || text.contains("doctorate")) return "PhD";
        if (text.contains("m.tech") || text.contains("masters") || text.contains("mca")) return "Masters";
        if (text.contains("b.tech") || text.contains("bachelor") || text.contains("bsc")) return "Bachelors";
        if (text.contains("diploma")) return "Diploma";

        return "Unknown";
    }

    public static int educationScore(String edu) {
        return switch (edu) {
            case "PhD" -> 100;
            case "Masters" -> 85;
            case "Bachelors" -> 70;
            case "Diploma" -> 50;
            default -> 40;
        };
    }
}
