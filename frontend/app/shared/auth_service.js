'use strict';

(function () {
    app.factory('AuthService', function ($http, $log, $base64, Session, SessionStorage, SESSION_KEYS) {
        var authService = {};

        authService.login = function (credentials) {
            var authData = $base64.encode(credentials.username + ":" + credentials.password);
            $http.defaults.headers.post = {
                'Content-Type': 'application/json',
                'Authorization': 'basic ' + authData
            };

            return $http
                .post('/user/login', {})
                .then(function (response) {
                    var data = response.data;
                    Session.create(data.token, data.user);
                    SessionStorage.setObject(SESSION_KEYS.authSession, Session);
                    return data;
                });
        };

        authService.logout = function () {
            SessionStorage.setObject(SESSION_KEYS.authSession, null);
        }

        authService.isAuthenticated = function () {
            return !!Session.user;
        };

        authService.isAuthorized = function (authorizedRoles) {
            if (!angular.isArray(authorizedRoles)) {
                authorizedRoles = [authorizedRoles];
            }
            $log.log("Authorizing...");
            $log.log("Session user : " + Session.user);
            return (authService.isAuthenticated() && authorizedRoles.indexOf(Session.user.role) != -1);
        };


        return authService;
    });
})();
