package com.example.buysell.services;

import com.example.buysell.dto.UserRegisterDto;
import com.example.buysell.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User save(UserRegisterDto userRegDto);
}
