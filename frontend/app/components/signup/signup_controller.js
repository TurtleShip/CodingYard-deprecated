(function () {

    app.controller('SignUpController', function ($scope, $log, User) {

        $scope.message = null;
        $scope.error = null;
        $scope.signedup = false;

        $scope.request = {
            username: null,
            password: null,
            firstName: null,
            lastName: null,
            email: null
        };

        $scope.createUser = function (request) {

            // put some pre check on the request
            $log.info("Create user called.");
            //User.createUser(request,
            //    function (response) {
            //        $scope.message = response;
            //        $scope.error = null;
            //        $scope.signedup = true;
            //    },
            //    function (response) {
            //        $scope.message = null;
            //        $scope.error = response;
            //        $scope.signedup = false;
            //    }
            //);
        };
    });
})();