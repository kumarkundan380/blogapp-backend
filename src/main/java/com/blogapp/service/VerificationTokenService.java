package com.blogapp.service;

import com.blogapp.model.User;
import com.blogapp.model.VerificationToken;

public interface VerificationTokenService {

    public VerificationToken getVerificationTokenByToken(String token);

    public VerificationToken saveToken(VerificationToken verificationToken);

    public VerificationToken createVerificationToken();

    public void removeVerificationToken(VerificationToken verificationToken);

    public VerificationToken getVerificationTokenByUser(User user);

}
