package com.codingyard.resources;

import com.codingyard.api.entity.auth.CodingyardToken;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.api.entity.user.Role;
import com.codingyard.auth.TokenAuthenticator;
import com.codingyard.auth.UserCredentialAuthenticator;
import com.codingyard.manager.UserManager;
import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.ChainedAuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.auth.oauth.OAuthFactory;
import org.junit.Before;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResourceTest {

    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final UserManager userManager = mock(UserManager.class);
    protected static final UserCredentialAuthenticator userCredentialAuthenticator = mock(UserCredentialAuthenticator.class);
    protected static final TokenAuthenticator tokenAuthenticator = mock(TokenAuthenticator.class);
    protected static final ChainedAuthFactory<CodingyardUser> chainedAuthFactory = new ChainedAuthFactory<>(
        new BasicAuthFactory<>(userCredentialAuthenticator, "Basic User Auth", CodingyardUser.class),
        new OAuthFactory<>(tokenAuthenticator, "Bearer User OAuth", CodingyardUser.class)
    );

    protected CodingyardUser globalAdmin;
    protected CodingyardUser admin;
    protected CodingyardUser member;
    protected CodingyardUser guest;
    protected CodingyardUser nonExistingUser;
    protected long id = 1;

    @Before
    public void setup() throws Exception {
        globalAdmin = new CodingyardUser.Builder("turtleship", "safe_password").role(Role.GLOBAL_ADMIN).build();
        admin = new CodingyardUser.Builder("admin_user", "secure_pwd").role(Role.ADMIN).build();
        member = new CodingyardUser.Builder("good_member", "cracking-code").role(Role.MEMBER).build();
        guest = new CodingyardUser.Builder("fresh-guest", "let-me-in").role(Role.GUEST).build();
        nonExistingUser = new CodingyardUser.Builder("who-am-i", "no-one").build();

        setupUser(globalAdmin);
        setupUser(admin);
        setupUser(member);
        setupUser(guest);

        setupNonExistingUser(nonExistingUser);
    }

    protected String bearerToken(final String token) {
        return String.format("bearer %s", token);
    }

    private void setupUser(final CodingyardUser user) throws AuthenticationException {
        user.setId(id++);
        user.setToken(CodingyardToken.Builder.build());
        mockUser(user);
    }

    private void mockUser(final CodingyardUser user) throws AuthenticationException {
        when(userManager.findById(user.getId())).thenReturn(Optional.of(user));
        when(userManager.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(userCredentialAuthenticator.authenticate(new BasicCredentials(user.getUsername(), user.getPassword()))).thenReturn(Optional.of(user));
        when(tokenAuthenticator.authenticate(user.getToken().getValue())).thenReturn(Optional.of(user));
    }

    private void setupNonExistingUser(final CodingyardUser user) throws AuthenticationException {
        user.setId(0L);
        user.setToken(CodingyardToken.Builder.build());
        when(userManager.findById(user.getId())).thenReturn(Optional.absent());
        when(userManager.findByUsername(user.getUsername())).thenReturn(Optional.absent());
        when(userCredentialAuthenticator.authenticate(new BasicCredentials(user.getUsername(), user.getPassword()))).thenReturn(Optional.absent());
        when(tokenAuthenticator.authenticate(user.getToken().getValue())).thenReturn(Optional.absent());
    }
}
