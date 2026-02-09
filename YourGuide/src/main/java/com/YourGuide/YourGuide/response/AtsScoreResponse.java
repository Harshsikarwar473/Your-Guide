package com.YourGuide.YourGuide.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class AtsScoreResponse {
    private int score; // 0-100

    private double keywordScore;     // 0-1
    private double skillScore;       // 0-1
    private double experienceScore;  // 0-1
    private double educationScore;   // 0-1
    private double contextScore;     // 0-1

    private List<String> matchedKeywords;
    private List<String> missingKeywords;

    private String summary;
    private List<String> suggestions;

}
