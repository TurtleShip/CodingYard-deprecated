'user strict';

(function () {
    app.controller('UserController', function ($routeParams, $scope, $log, User, USER_ROLES) {
        var userId = $routeParams.userId;
        $scope.user = null;
        $scope.errorMessage = null;
        $scope.userRoles = USER_ROLES;

        var success = function (data) {
            $log.log("call success");
            $log.log("Received : " + data);
            $scope.user = data;

        };
        var error = function (data) {
            $log.log("Call failed");
            $log.log("status : " + data.status);
            $scope.errorMessage = "There is no user with id " + userId;
        };

        User.get({id: userId}, success, error);
    })
})();
