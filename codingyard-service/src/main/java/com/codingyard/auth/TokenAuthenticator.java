package com.codingyard.auth;

import com.codingyard.dao.TokenDAO;
import com.codingyard.api.entity.auth.CodingyardToken;
import com.codingyard.api.entity.user.CodingyardUser;
import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

public class TokenAuthenticator implements Authenticator<String, CodingyardUser> {

    private final TokenDAO tokenDAO;

    public TokenAuthenticator(final TokenDAO tokenDAO) {
        this.tokenDAO = tokenDAO;
    }


    @Override
    public Optional<CodingyardUser> authenticate(final String credentials) throws AuthenticationException {
        Optional<CodingyardToken> searchResult = tokenDAO.findByTokenValue(credentials);
        if (searchResult.isPresent()) {
            return Optional.of(searchResult.get().getUser());
        } else {
            return Optional.absent();
        }
    }
}
