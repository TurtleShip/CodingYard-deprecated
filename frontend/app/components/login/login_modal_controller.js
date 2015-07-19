'use strict';

(function () {
    app.controller('LoginModalController', function ($scope, $log, $modalInstance, AuthService) {
        $scope.login = function (credential) {

            var loggedIn = AuthService.login(credential);

            loggedIn.then(
                function successful() {
                    $modalInstance.close();
                },
                function loginFailed(error) {
                    $log.info("LOGIN FAILED! Status code : " + error.status);
                }
            );
        };
    });
})();