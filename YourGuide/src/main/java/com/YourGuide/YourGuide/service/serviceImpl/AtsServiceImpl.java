package com.YourGuide.YourGuide.service.serviceImpl;

import com.YourGuide.YourGuide.model.Job;
import com.YourGuide.YourGuide.model.Resume;
import com.YourGuide.YourGuide.model.User;
import com.YourGuide.YourGuide.repo.JobRepo;
import com.YourGuide.YourGuide.repo.ResumeRepo;
import com.YourGuide.YourGuide.repo.UserRepo;
import com.YourGuide.YourGuide.request.AtsScoreRequest;
import com.YourGuide.YourGuide.response.AtsScoreResponse;
import com.YourGuide.YourGuide.response.LlmResult;
import com.YourGuide.YourGuide.service.AtsService;
import com.YourGuide.YourGuide.service.LlmService;
import com.YourGuide.YourGuide.utils.ResumeParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AtsServiceImpl implements AtsService {

    private final JobRepo jobRepo;
    private final UserRepo userRepo;
    private final ResumeRepo resumeRepo;
    private final LlmService llmService;

    // weights (tuneable)
    private static final double KEYWORD_WEIGHT = 0.50;
    private static final double SKILL_WEIGHT   = 0.20;
    private static final double CONTEXT_WEIGHT = 0.15;
    private static final double EXP_WEIGHT     = 0.10;
    private static final double EDU_WEIGHT     = 0.05;
    @Override
    public AtsScoreResponse evaluateForJob(Long jobId, String loggedInEmail, AtsScoreRequest request) {
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        User user = userRepo.findByEmail(loggedInEmail);
        if (user == null) throw new RuntimeException("User not found");

        Resume resume = resumeRepo.findByUser(user);
        if (resume == null) throw new RuntimeException("Resume not uploaded yet");

        String resumeText = (request.getResumeText() != null && !request.getResumeText().isBlank())
                ? request.getResumeText()
                : resume.getExtractedText();

        String jdText = (request.getJobDescription() != null && !request.getJobDescription().isBlank())
                ? request.getJobDescription()
                : job.getDescription();

        return evaluateInternal(resumeText, jdText);
    }

    @Override
    public AtsScoreResponse evaluateDirect(AtsScoreRequest request) {
        if (request.getResumeText() == null || request.getResumeText().isBlank())
            throw new RuntimeException("Resume text is required");

        if (request.getJobDescription() == null || request.getJobDescription().isBlank())
            throw new RuntimeException("Job description is required");

        return evaluateInternal(request.getResumeText(), request.getJobDescription());
    }

    private AtsScoreResponse evaluateInternal(String resumeText, String jdText) {

        String resumeLower = resumeText.toLowerCase();
        String jdLower = jdText.toLowerCase();

        // 1) Extract keywords & skills
        Set<String> jdKeywords = extractKeywords(jdLower);
        Set<String> jdSkills = extractSkills(jdLower);

        // 2) Keyword match
        List<String> matchedKeywords = new ArrayList<>();
        List<String> missingKeywords = new ArrayList<>();

        for (String kw : jdKeywords) {
            if (resumeLower.contains(kw)) matchedKeywords.add(kw);
            else missingKeywords.add(kw);
        }

        double keywordScore = jdKeywords.isEmpty() ? 1.0 : (double) matchedKeywords.size() / jdKeywords.size();

        // 3) Skill match
        int matchedSkills = 0;
        for (String s : jdSkills) {
            if (resumeLower.contains(s)) matchedSkills++;
        }
        double skillScore = jdSkills.isEmpty() ? 1.0 : (double) matchedSkills / jdSkills.size();

        // 4) Experience score
        double resumeExp = ResumeParser.extractExperienceYears(resumeText);
        double jdExp = ResumeParser.extractExperienceYears(jdText);
        double experienceScore = (jdExp == 0) ? 1.0 : Math.min(resumeExp / jdExp, 1.0);

        // 5) Education score
        String edu = ResumeParser.extractEducation(resumeText);
        double educationScore = ResumeParser.educationScore(edu);

        // 6) LLM contextual score + suggestions
        LlmResult llm = llmService.getAtsInsights(resumeText, jdText, missingKeywords);
        double contextScore = clamp01(llm.getContextScore());

        // 7) Final weighted score
        double final01 =
                (KEYWORD_WEIGHT * keywordScore) +
                        (SKILL_WEIGHT   * skillScore) +
                        (CONTEXT_WEIGHT * contextScore) +
                        (EXP_WEIGHT     * experienceScore) +
                        (EDU_WEIGHT     * educationScore);

        int finalScore = (int) Math.round(final01 * 100);

        return AtsScoreResponse.builder()
                .score(finalScore)
                .keywordScore(keywordScore)
                .skillScore(skillScore)
                .experienceScore(experienceScore)
                .educationScore(educationScore)
                .contextScore(contextScore)
                .matchedKeywords(matchedKeywords)
                .missingKeywords(missingKeywords.stream().limit(25).toList())
                .summary(llm.getSummary())
                .suggestions(llm.getSuggestions())
                .build();
    }

    private double clamp01(double x) {
        if (x < 0) return 0;
        if (x > 1) return 1;
        return x;
    }

    private Set<String> extractKeywords(String text) {
        String cleaned = text.replaceAll("[^a-z0-9 ]", " ");
        String[] tokens = cleaned.split("\\s+");

        Set<String> stop = Set.of(
                "and", "the", "with", "for", "your", "you", "that", "this",
                "will", "are", "our", "we", "have", "has", "from", "into"
        );

        return Arrays.stream(tokens)
                .map(String::trim)
                .filter(t -> t.length() > 3)
                .filter(t -> !stop.contains(t))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<String> extractSkills(String text) {
        // Expand this list later
        List<String> known = List.of(
                "java", "spring", "spring boot", "hibernate", "jpa",
                "mysql", "sql", "rest", "microservices",
                "docker", "kubernetes", "aws",
                "react", "node", "mongodb",
                "git", "linux"
        );

        Set<String> found = new LinkedHashSet<>();
        for (String s : known) {
            if (text.contains(s)) found.add(s);
        }
        return found;
    }
}
