package com.YourGuide.YourGuide.controller;



import com.YourGuide.YourGuide.request.AtsScoreRequest;
import com.YourGuide.YourGuide.response.AtsScoreResponse;
import com.YourGuide.YourGuide.service.AtsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ats")
@CrossOrigin("*")
public class AtsController {

    private final AtsService atsService;

    // Real flow: user selects a job from DB and system uses his uploaded resume
    @PostMapping("/evaluate/job/{jobId}")
    public ResponseEntity<AtsScoreResponse> evaluateForJob(
            @PathVariable Long jobId,
            @RequestBody(required = false) AtsScoreRequest request,
            Authentication auth
    ) {
        System.out.println("ATS CONTROLLER HITTTTT");
        if (request == null) request = new AtsScoreRequest();
        String email = auth.getName();
        return ResponseEntity.ok(atsService.evaluateForJob(jobId, email, request));
    }

    // Direct evaluate: user pastes resume + JD
    @PostMapping("/evaluate")
    public ResponseEntity<AtsScoreResponse> evaluateDirect(@RequestBody AtsScoreRequest request) {
        return ResponseEntity.ok(atsService.evaluateDirect(request));
    }
}
