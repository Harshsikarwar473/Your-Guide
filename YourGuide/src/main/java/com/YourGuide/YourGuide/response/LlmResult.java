package com.YourGuide.YourGuide.response;


import lombok.Data;

import java.util.List;

@Data
public class LlmResult {
    private double contextScore;
    private String summary;
    private List<String> suggestions;
}
