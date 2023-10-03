package com.blogapp.service.impl;

import com.blogapp.exception.BlogAppException;
import com.blogapp.exception.ResourceNotFoundException;
import com.blogapp.model.User;
import com.blogapp.model.VerificationToken;
import com.blogapp.repository.VerificationTokenRepository;
import com.blogapp.service.VerificationTokenService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import static com.blogapp.constant.BlogAppConstant.EXCEPTION_FIELD;
import static com.blogapp.constant.BlogAppConstant.USER_EXCEPTION;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR= KeyGenerators.secureRandom(64);
    @Autowired
    public VerificationTokenServiceImpl(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public VerificationToken getVerificationTokenByToken(String token) {
        return verificationTokenRepository.findByToken(token).orElseThrow(()-> new BlogAppException("Token is either expired or invalid"));
    }

    @Override
    public VerificationToken saveToken(VerificationToken verificationToken) {
        return verificationTokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken createVerificationToken() {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(new String(Base64.encodeBase64URLSafeString(DEFAULT_TOKEN_GENERATOR.generateKey())));
        return verificationToken;
    }

    @Override
    public void removeVerificationToken(VerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }

    @Override
    public VerificationToken getVerificationTokenByUser(User user) {
        return verificationTokenRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException(USER_EXCEPTION, EXCEPTION_FIELD , user.getUserId()));
    }
}
