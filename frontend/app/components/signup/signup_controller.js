(function () {

    app.controller('SignUpController', function ($scope, $modal, $log, User) {

        $scope.signedup = false;
        $scope.signupFailed = false;
        $scope.isUsernameAvailable = false;
        $scope.usernameCheckMessage = null;

        $scope.request = {
            username: null,
            password: null,
            firstName: null,
            lastName: null,
            email: null
        };

        $scope.openLogin = function () {
            $modal.open({
                animation: true,
                templateUrl: '/app/components/login/login-modal.html?bust=' + Math.random().toString(36).slice(2),
                controller: 'LoginModalController',
                size: 'sm'
            });
        };

        $scope.checkUsernameAvailable = function (username) {
            $scope.usernameCheckMessage = "Checking for availability...";
            User.isUsernameAvailable({
                username: username
            }).$promise
                .then(function (response) {
                    $scope.isUsernameAvailable = response.isAvailable;
                    if ($scope.isUsernameAvailable) {
                        $scope.usernameCheckMessage = username + " is available!";
                    } else {
                        $scope.usernameCheckMessage = "Sorry, " + username + " is already taken.";
                    }
                })
                .catch(function () {
                    $scope.usernameCheckMessage = "Failed to check for availability.";
                });
        };

        $scope.createUser = function (request) {

            User.createUser(request)
                .$promise
                .then(function success() {
                    $scope.signedup = true;
                    $scope.signupFailed = false;
                })
                .catch(function (response) {
                    // it is possible that the request failed because username is taken.
                    $scope.checkUsernameAvailable($scope.request.username);
                    $log.warn("Signup for server responded with " + response.status);

                    $scope.signedup = false;
                    $scope.signupFailed = true;
                });
        };
    });
})();