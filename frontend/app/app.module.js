(function () {
    var app = angular.module('codingyard', ['base64']);
    var sessionKey = 'session';

    app.controller('CodingyardController', function ($scope, $log, USER_ROLES, AuthService, Session, SessionStorage) {
        $scope.currentUser = null;
        $scope.token = null;
        $scope.userRoles = USER_ROLES;
        $scope.isAuthorized = AuthService.isAuthorized;

        // check if user's has some session
        var info = SessionStorage.getObject(sessionKey, undefined);

        if (info) {
            $scope.currentUser = info.user;
            $scope.token = info.token;
            Session.create(info.token, info.user);
        }
        $scope.setCurrentUser = function (user, token) {
            $scope.currentUser = user;
            $scope.token = token;
        };

    });

    app.controller('LoginController', function ($scope, $rootScope, $log, AUTH_EVENTS, AuthService) {

        $scope.credentials = {
            username: '',
            password: ''
        };

        $scope.login = function (credentials) {
            AuthService.login(credentials)
                .then(
                function (data) { // success call back
                    $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
                    $scope.setCurrentUser(data.user, data.token);
                },
                function () { // error call back
                    $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
                });
        };

        $scope.logout = function() {
            AuthService.logout();
            $scope.currentUser = null;
            $scope.token = null;
        }
    });


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

    app.factory('AuthService', function ($http, $log, $base64, Session, SessionStorage) {
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
                    SessionStorage.setObject(sessionKey, Session);
                    return data;
                });
        };

        authService.logout = function() {
            SessionStorage.setObject(sessionKey, null);
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

    // Provides method to store and load session info to client-side (a.k.a browser )
    app.factory('SessionStorage', function ($window) {
        return {
            set: function (key, value) {
                $window.localStorage[key] = value;
            },
            get: function (key, defaultValue) {
                return $window.localStorage[key] || defaultValue;
            },
            setObject: function (key, value) {
                $window.localStorage[key] = JSON.stringify(value);
            },
            getObject: function (key) {
                return JSON.parse($window.localStorage[key] || '{}');
            }
        }
    });

    app.service('Session', function ($log) {
        this.create = function (token, user) {
            $log.log("token : " + token);
            $log.log("user  : " + user);
            this.token = token;
            this.user = user;
        };

        this.destroy = function () {
            this.token = null;
            this.user = null;
        }
    });
})();
