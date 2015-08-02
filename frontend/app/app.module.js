'use strict';

var app = angular.module('codingyard', ['ngRoute', 'ngResource',
    'base64', 'ui.ace', 'ui.bootstrap', 'ui.router', 'smart-table',
    'xeditable']);

(function () {

    /**
     * This is the root controller of www.codingyard.com
     */
    app.controller('CodingyardController', function ($rootScope, $scope, $route, $routeParams, $location, $log,
                                                     USER_ROLES, AuthService, Session, SessionStorage,
                                                     SESSION_KEYS, AUTH_EVENTS, ALERT_EVENTS, User, SharedData) {

        $scope.$route = $route;
        $scope.$location = $location;
        $scope.$routeParams = $routeParams;

        $scope.token = null;
        $scope.userRoles = USER_ROLES;
        $scope.isAuthorized = AuthService.isAuthorized;

        $scope.sharedData = SharedData.getSharedData();

        var getMyInfo = function () {
            User.getMyInfo({},
                function success(user) {
                    $scope.sharedData.currentUser = user;
                    $rootScope.$broadcast(AUTH_EVENTS.gotBasicUserInfo, user);
                },
                function error(response) {
                    $log.info("Failed to retrieve user information. Response : " + response);
                }
            );
        };

        // check if the current user has been authenticated before
        var oauthToken = SessionStorage.get(SESSION_KEYS.token, undefined);

        if (!(angular.isUndefined(oauthToken) || oauthToken == null)) {
            AuthService.setBearerOauthHeader(oauthToken);
            getMyInfo();
        }

        // Get the current user's information when he is successfully authenticated
        $scope.$on(AUTH_EVENTS.loginSuccess, getMyInfo);

        // Reset the current user's information when user logs out
        $scope.$on(AUTH_EVENTS.logout, function () {
            $scope.sharedData.currentUser = null;
            $scope.token = null;
        });

        $scope.alerts = [];

        $scope.addAlert = function (event, isWarning, msg) {
            $log.info("YO!");
            $scope.alerts.push({
                type: isWarning ? 'danger' : 'success',
                msg: msg
            });
        };

        $scope.closeAlert = function (index) {
            $scope.alerts.splice(index, 1);
        };

        $scope.$on(ALERT_EVENTS.alertFired, $scope.addAlert);

    });
})();
