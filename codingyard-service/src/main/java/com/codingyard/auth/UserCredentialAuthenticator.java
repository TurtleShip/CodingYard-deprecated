package com.codingyard.auth;

import com.codingyard.dao.UserDAO;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.util.Encryptor;
import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;


public class UserCredentialAuthenticator implements Authenticator<BasicCredentials, CodingyardUser> {

    private final UserDAO userDAO;

    public UserCredentialAuthenticator(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Optional<CodingyardUser> authenticate(final BasicCredentials credentials) throws AuthenticationException {
        final Optional<CodingyardUser> searchResult = userDAO.findByUsername(credentials.getUsername());

        if (searchResult.isPresent() && Encryptor.isSame(credentials.getPassword(), searchResult.get().getPassword())) {
            return searchResult;
        } else {
            return Optional.absent();
        }
    }
}
