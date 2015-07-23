(function () {

    app.controller('SignUpController', function ($scope, $modal, $log, User) {

        $scope.signedup = false;
        $scope.signupFailed = false;
        $scope.isUsernameAvailable = false;
        $scope.usernameCheckMessage = null;
        $scope.isEmailAvailable = false;
        $scope.emailCheckMessage = null;

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

        $scope.checkEmailAvailable = function (email) {
            $scope.emailCheckMessage = "Checking for availability...";
            User.isEmailAvailable({
                email: email
            }).$promise
                .then(function (response) {
                    $scope.isEmailAvailable = response.isAvailable;
                    if ($scope.isEmailAvailable) {
                        $scope.emailCheckMessage = email + " is available!";
                    } else {
                        $scope.emailCheckMessage = email + " is already associated with an account.";
                    }

                })
                .catch(function () {
                    $scope.emailCheckMessage = "Failed to check for availability.";
                });
        }

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