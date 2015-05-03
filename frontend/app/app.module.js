'use strict';

var app = angular.module('codingyard', ['ngRoute', 'ngResource', 'base64']);

(function () {

    app.controller('CodingyardController', function ($scope, $route, $routeParams, $location, $log, USER_ROLES, AuthService, Session, SessionStorage, SESSION_KEYS) {

        $scope.$route = $route;
        $scope.$location = $location;
        $scope.$routeParams = $routeParams;

        $scope.currentUser = null;
        $scope.token = null;
        $scope.userRoles = USER_ROLES;
        $scope.isAuthorized = AuthService.isAuthorized;

        // check if user's has some session
        var info = SessionStorage.getObject(SESSION_KEYS.authSession, undefined);

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

        $scope.logout = function () {
            AuthService.logout();
            $scope.currentUser = null;
            $scope.token = null;
        }
    });

})();
