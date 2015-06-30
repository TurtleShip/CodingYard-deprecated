'use strict';

/**
 * This class is responsible for Authentication and Authorization of the current user.
 */
(function () {
    app.factory('AuthService', function ($rootScope, $http, $log, $base64, User, Session, SessionStorage, SESSION_KEYS, AUTH_EVENTS) {
        var authService = {};

        /**
         * Sets base64 encoded Basic Authorization header.
         *
         * @param credential Object with {username: <username>, password: <password>}
         */
        var setBasicAuthHeader = function (credential) {
            var authData = $base64.encode(credential.username + ":" + credential.password);
            $http.defaults.headers.common = {
                Authorization: 'basic ' + authData
            };
        };

        /**
         * Sets Authorization header with given bearer token
         * @param token Bearer token. String.
         */
        var setBearerOauthHeader = function (token) {
            $http.defaults.headers.common = {
                Authorization: 'bearer ' + token
            };
        };

        /**
         * Unset Authorization header
         */
        var unsetAuthHeader = function () {
            $http.defaults.headers.common = {};
        };

        authService.setBasicAuthHeader = setBasicAuthHeader;
        authService.setBearerOauthHeader = setBearerOauthHeader;
        authService.unsetAuthHeader = unsetAuthHeader;
        /**
         * Log in with the given credential.
         * When login is successful, it will set bearer token.
         *
         * @param credentials Object with {username: <username>, password: <password>}
         */
        authService.login = function (credentials) {
            setBasicAuthHeader(credentials);

            $http.get('/api/user/login')
                .then(
                function success(response) {
                    var token = response.data;
                    setBearerOauthHeader(token);
                    SessionStorage.set(SESSION_KEYS.token, token);
                    $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
                },
                function () {
                    unsetAuthHeader();
                    $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
                }
            );
        };

        authService.logout = function () {
            SessionStorage.remove(SESSION_KEYS.token);
            $rootScope.$broadcast(AUTH_EVENTS.logout);
            unsetAuthHeader();
        };

        authService.isAuthenticated = function () {
            return !!SessionStorage.get(SESSION_KEYS.token, false);
        };

        /**
         * Checks if the user has authorized roles
         * @param authorizedRoles authorized roles
         * @param user user to be authorized
         * @returns {*|boolean} true is user has a role that belongs to authorized roles
         */
        authService.isAuthorized = function (authorizedRoles, user) {
            if (!user) {
                return false;
            }
            if (!angular.isArray(authorizedRoles)) {
                authorizedRoles = [authorizedRoles];
            }

            return (authService.isAuthenticated() && authorizedRoles.indexOf(user.role) != -1);
        };


        return authService;
    });
})();
