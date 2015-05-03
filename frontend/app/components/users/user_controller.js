'user strict';

(function () {
    app.controller('UserController', function ($routeParams, $scope, $log, User) {
        var userId = $routeParams.userId;
        $scope.userData = null;
        $scope.errorMessage = null;

        var success = function (data) {
            $log.log("call success");
            $log.log("Received : " + data);
            $scope.userData = data;

        };
        var error = function (data) {
            $log.log("Call failed");
            $log.log("status : " + data.status);
            $scope.errorMessage = "There is no user with id " + userId;
        };

        User.get({id: userId}, success, error);
    })
})();
