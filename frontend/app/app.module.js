(function () {
    var app = angular.module('codingyard', ['base64']);
    app.controller('CodingyardController', function () {
        this.seulgi = {
            "firstName": "Seulgi",
            "lastName": "Kim",
            "age": 3
        };
    });

    app.controller('UserController', ['$http', '$log', '$base64', function ($http, $log, $base64) {
        this.authToken = "don't have it yet";
        this.createUser = function () {
            // TODO: Implement me
            alert("create user not implemented yet.");
        };

        this.login = function (credential) {
            //alert("Login not implemented yet. Username : " + credential.username + ", password : " + credential.password);
            this.authToken = "getting token...";
            var authData = $base64.encode(credential.username + ":" + credential.password);
            $log.log(authData);
            $http.defaults.headers.post = {
                'Content-Type': 'application/json',
                'Authorization': 'basic ' + authData
            };
            var cur = this;
            $http.post('/user/login', {})
                .success(function (data) {
                    $log.log("authentication success. Got token : " + data);
                    cur.authToken = data;
                });
        };
    }]);

    // shameless copy from a medium blog post : https://medium.com/opinionated-angularjs/techniques-for-authentication-in-angularjs-applications-7bbf0346acec
    app.constant('USER_ROLES', {
        globalAdmin: 'global-admin',
        admin: 'admin'
    });

    app.constant('AUTH_EVENTS', {
        loginSuccess: 'auth-login-success',
        loginFailed: 'auth-login-failed',
        logoutSuccess: 'auth-logout-success',
        sessionTimeout: 'auth-session-timeout',
        notAuthenticated: 'auth-not-authenticated',
        notAuthorized: 'auth-not-authorized'
    });

    app.factory('AuthService', function($http, $log, $base64, Session) {
        var authService = {};

        authService.login = function(credentials) {
            var authData = $base64.encode(credential.username + ":" + credential.password);
            $http.defaults.headers.post = {
                'Content-Type': 'application/json',
                'Authorization': authData
            };

            return $http
                .post('/user/login', {})
                .then(function(token) {
                    $log.log("received token : " + token);
                    Session.create(token);
                    return token;
                })
        };

        authService.isAuthenticated = function() {
            return !!Session.token;
        };

        return authService;
    });

    //app.service('Session', function() {
    //    this.create = function(sessionId, )
    //})


    // end of shameless copy
})();