'use strict';

(function () {
    app.controller('UserDeleteConfirmController', function ($scope, $modalInstance, userToDelete) {
        $scope.user = userToDelete;

        $scope.ok = function () {
            $modalInstance.close($scope.user);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

    });
})();