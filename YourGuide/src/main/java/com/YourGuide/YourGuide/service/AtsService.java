package com.YourGuide.YourGuide.service;

import com.YourGuide.YourGuide.request.AtsScoreRequest;
import com.YourGuide.YourGuide.response.AtsScoreResponse;

public interface AtsService {
    AtsScoreResponse evaluateForJob(Long jobId, String loggedInEmail, AtsScoreRequest request);
    AtsScoreResponse evaluateDirect(AtsScoreRequest request);
}
