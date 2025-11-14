package com.YourGuide.YourGuide.model;

import com.YourGuide.YourGuide.roles.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class Verification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private  long id ;

    private String otp ;

    private String email ;

    private Role role ;
}
