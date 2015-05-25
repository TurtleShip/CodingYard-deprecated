'use strict';

(function () {
    app.controller('LoginController', function ($scope, $modal, $log, AuthService) {

        $scope.credentials = {
            username: '',
            password: ''
        };

        $scope.openLogin = function () {
            $modal.open({
                animation: true,
                templateUrl: '/app/components/login/login-modal.html?bust=' + Math.random().toString(36).slice(2),
                controller: 'LoginModalController',
                size: 'sm'
            });
        };
        $scope.logout = AuthService.logout;
    });
})();