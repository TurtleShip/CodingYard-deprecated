'use strict';

(function () {
    app.controller('LoginModalController', function ($scope, $log, $modalInstance, AuthService) {
        $scope.login = function (credential) {
            AuthService.login(credential);
            $modalInstance.close();
        };
    });
})();