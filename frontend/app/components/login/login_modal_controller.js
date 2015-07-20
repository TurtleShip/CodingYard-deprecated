'use strict';

(function () {
    app.controller('LoginModalController', function ($scope, $log, $modalInstance, AuthService) {

        $scope.message = null;

        $scope.login = function (credential) {
            var loggedIn = AuthService.login(credential);

            loggedIn.then(
                function successful() {
                    $modalInstance.close();
                },
                function loginFailed(error) {
                    if (error.status === 401) {
                        $scope.message = "Invalid credential :(";
                    } else {
                        $scope.message = "Oops. Error on our side. Try again, please.";
                    }

                }
            );
        };
    });
})();