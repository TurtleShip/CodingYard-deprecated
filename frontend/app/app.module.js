'use strict';

var app = angular.module('codingyard', ['ngRoute', 'ngResource', 'base64', 'ui.ace', 'ui.bootstrap']);

(function () {

    /**
     * This is the root controller of www.codingyard.com
     */
    app.controller('CodingyardController', function ($scope, $route, $routeParams, $location, $log, USER_ROLES, AuthService, Session, SessionStorage, SESSION_KEYS, AUTH_EVENTS, User, SharedData) {

        $scope.$route = $route;
        $scope.$location = $location;
        $scope.$routeParams = $routeParams;

        $scope.currentUser = SharedData.getCurrentUser();
        $scope.token = null;
        $scope.userRoles = USER_ROLES;
        $scope.isAuthorized = AuthService.isAuthorized;

        var getMyInfo = function () {
            User.getMyInfo({},
                function success(user) {
                    $scope.currentUser = user;
                    SharedData.setCurrentUser(user);
                    $log.info("Successfully retrieved user information.");
                },
                function error(response) {
                    $log.info("Failed to retrieve user information. Response : " + response);
                }
            );
        };

        // check if the current user has been authenticated before
        var oauthToken = SessionStorage.get(SESSION_KEYS.token, undefined);

        if (!(angular.isUndefined(oauthToken) || oauthToken == null)) {
            $log.info("Found oauth token in session : " + oauthToken);
            AuthService.setBearerOauthHeader(oauthToken);
            getMyInfo();
        }

        // Get the current user's information when he is successfully authenticated
        $scope.$on(AUTH_EVENTS.loginSuccess, getMyInfo);

        // Reset the current user's information when user logs out
        $scope.$on(AUTH_EVENTS.logout, function () {
            $scope.currentUser = null;
            $scope.token = null;
        });
    });
})();
