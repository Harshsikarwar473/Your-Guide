package com.YourGuide.YourGuide.service;

import com.YourGuide.YourGuide.response.LlmResult;

import java.util.List;

public interface LlmService {
    LlmResult getAtsInsights(String resumeText, String jobDescription, List<String> missingKeywords);
}
