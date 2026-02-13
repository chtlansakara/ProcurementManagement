package com.cht.procurementManagement.services.auth;

import com.cht.procurementManagement.dto.SignupRequest;
import com.cht.procurementManagement.dto.UserDto;
import com.cht.procurementManagement.entities.User;

public interface AuthService {
    UserDto signupUser(SignupRequest signupRequest);
    boolean hasUserWithEmail(String email);
}
