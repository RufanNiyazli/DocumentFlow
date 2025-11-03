package com.project.documentflow.service;

import com.project.documentflow.entity.RefreshToken;
import com.project.documentflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRefreshTokenService{
    public RefreshToken createRefreshToken(User user);

}
