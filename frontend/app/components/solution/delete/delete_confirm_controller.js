'use strict';

(function () {
    app.controller('DeleteConfirmController', function ($scope, $modalInstance, solutionToDelete) {
        $scope.solution = solutionToDelete;

        $scope.ok = function () {
            $modalInstance.close($scope.solution);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

    });
})();

