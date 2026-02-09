package com.YourGuide.YourGuide.service.serviceImpl;

import com.YourGuide.YourGuide.response.LlmResult;
import com.YourGuide.YourGuide.service.LlmService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatCompletionMessageParam;
import com.openai.models.ChatCompletionSystemMessageParam;
import com.openai.models.ChatCompletionUserMessageParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LlmServiceImpl implements LlmService {

    private final OpenAIClient openAIClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    @Override
    public LlmResult getAtsInsights(String resumeText, String jobDescription, List<String> missingKeywords) {

        String prompt = """
                You are an ATS resume evaluator.
                You MUST return ONLY valid JSON (no markdown, no extra text).

                Task:
                - Evaluate resume vs job description
                - Give contextual match score between 0 and 1
                - Give 1-line summary
                - Give 5 JD-specific improvement suggestions

                Missing keywords: %s

                Return JSON in this exact format:
                {
                  "contextScore": 0.0,
                  "summary": "string",
                  "suggestions": ["s1","s2","s3","s4","s5"]
                }

                Resume:
                %s

                Job Description:
                %s
                """.formatted(String.join(", ", missingKeywords), resumeText, jobDescription);

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(model)
                .messages(List.of(
                        ChatCompletionMessageParam.ofSystem(
                                ChatCompletionSystemMessageParam.builder()
                                        .content("Return ONLY JSON. No extra text.")
                                        .build()
                        ),
                        ChatCompletionMessageParam.ofUser(
                                ChatCompletionUserMessageParam.builder()
                                        .content(prompt)
                                        .build()
                        )
                ))
                .temperature(0.2)
                .build();

        ChatCompletion completion = openAIClient.chat().completions().create(params);

        // content() returns Optional<String> in your SDK
        String raw = completion.choices().get(0).message().content().orElse("");

        try {
            return objectMapper.readValue(raw, LlmResult.class);
        } catch (Exception e) {
            LlmResult fallback = new LlmResult();
            fallback.setContextScore(0.5);
            fallback.setSummary("AI parsing failed, showing fallback insights.");
            fallback.setSuggestions(List.of(
                    "Add missing JD keywords in skills/projects if true.",
                    "Add 2–3 quantified project bullet points relevant to the JD.",
                    "Use strong action verbs and measurable impact.",
                    "Reorder sections: Skills → Projects → Experience.",
                    "Tailor resume summary to match the target job role."
            ));
            return fallback;
        }
    }
}
