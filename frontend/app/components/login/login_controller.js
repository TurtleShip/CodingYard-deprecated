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
                templateUrl: '/app/components/login/login-modal.html',
                controller: 'LoginModalController'
            });
        };
        $scope.logout = AuthService.logout;
    });
})();