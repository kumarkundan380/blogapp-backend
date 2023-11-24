package com.blogapp.service;

import com.blogapp.model.User;
import com.blogapp.model.VerificationToken;

public interface VerificationTokenService {

    VerificationToken getVerificationTokenByToken(String token);

    VerificationToken saveToken(VerificationToken verificationToken);

    VerificationToken createVerificationToken();

    void removeVerificationToken(VerificationToken verificationToken);

    VerificationToken getVerificationTokenByUser(User user);

}
