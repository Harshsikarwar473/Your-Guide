package com.YourGuide.YourGuide.request;

import com.YourGuide.YourGuide.roles.Role;
import lombok.Data;

@Data
public class SignUpRequest {
    private String email ;
    private String name ;
    private String otp  ;
    private Role role ;

}
