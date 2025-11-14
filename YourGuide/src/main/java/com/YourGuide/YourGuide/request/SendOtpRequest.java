package com.YourGuide.YourGuide.request;

import com.YourGuide.YourGuide.roles.Role;
import lombok.Data;

@Data
public class SendOtpRequest {
    private String email ;
    private Role role;
}
