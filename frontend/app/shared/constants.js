'use strict';

(function () {

    app.constant('USER_ROLES', {
        globalAdmin: 'GLOBAL_ADMIN',
        admin: 'ADMIN',
        member: 'MEMBER',
        guest: 'GUEST'
    });

    app.constant('AUTH_EVENTS', {
        loginSuccess: 'auth-login-success',
        loginFailed: 'auth-login-failed',
        logoutSuccess: 'auth-logout-success',
        sessionTimeout: 'auth-session-timeout',
        notAuthenticated: 'auth-not-authenticated',
        notAuthorized: 'auth-not-authorized'
    });

    app.constant('SESSION_KEYS', {
        authSession: 'auth-session'
    });

})();
