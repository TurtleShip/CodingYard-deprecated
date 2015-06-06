'use strict';

(function () {
    app.controller('UserController', function ($stateParams, $scope, $rootScope, $log, User, USER_ROLES, SharedData) {
        var userId = parseInt($stateParams.userId);
        $scope.user = null;
        $scope.userRoles = USER_ROLES;
        $scope.canEdit = false;
        $scope.errorMessage = null;

        $scope.user = SharedData.getCurrentUser();
        $scope.userId = userId;
        $log.info("user id : " + userId);
        if (!!$scope.user && $scope.user.id === userId) {
            $scope.canEdit = true;
        } else {
            User.get({id: userId},
                function (data) {
                    $scope.user = data;
                },
                function () {
                    $scope.errorMessage = "Sorry, we are having trouble loading user " + userId;
                }
            );
        }
    })
})();
