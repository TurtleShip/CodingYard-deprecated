'use strict';

(function () {
    app.controller('UVaViewController', function ($scope, $log, $modal, $filter,
                                                  UVa, AceEditor, SolutionPermission,
                                                  AuthService, AUTH_EVENTS, AlertService, $stateParams) {
        $scope.paginationSetting = {
            itemPerPage: 5,
            displayedPages: 10
        };

        $scope.pickedId = parseInt($stateParams.id);

        $scope.solutions = null;
        $scope.displayedSolutions = null;
        $scope.pickedSolution = null;

        $scope.supported = {
            languages: [
                {key: 'Any', value: ''},
                {key: 'Java', value: 'JAVA'},
                {key: 'Cpp', value: 'CPP'},
                {key: 'C', value: 'C'},
                {key: 'Python', value: 'PYTHON'},
                {key: 'Ruby', value: 'RUBY'},
                {key: 'Other', value: 'OTHER'}
            ]
        };

        $scope.solutionToDelete = null;

        $scope.deleteSolution = function (solution) {
            UVa.deleteSolution({id: solution.id},
                function () {
                    var index = $scope.solutions.indexOf(solution);
                    if (index !== -1) {
                        $scope.solutions.splice(index, 1);
                    }
                });
        };

        $scope.openConfirm = function (solution) {
            $scope.solutionToDelete = solution;
            var modalInstance = $modal.open({
                animation: true,
                templateUrl: '/app/components/solution/delete/delete_confirm.html?bust=' + Math.random().toString(36).slice(2),
                controller: 'DeleteConfirmController',
                size: 'sm',
                resolve: {
                    solutionToDelete: function () {
                        return $scope.solutionToDelete;
                    }
                }
            });

            modalInstance.result.then($scope.deleteSolution);
        };

        $scope.aceOption = AceEditor.getSetting();
        $scope.aceOption["onLoad"] = function (_ace) {
            $scope.modeChanged = function () {
                if ($scope.pickedSolution) {
                    _ace.getSession().setMode("ace/mode/" + AceEditor.getMode($scope.pickedSolution.language));
                }
            };

            _ace.$blockScrolling = Infinity;
        };

        $scope.getSolutions = function () {
            UVa.findAll({},
                function (response) { // success
                    $scope.solutions = response;
                    $scope.populatePermission();
                    if ($scope.solutions.length == 0) {
                        // Having no content is not an error. So use success instead of warning here.
                        AlertService.fireSuccess("Sorry. We don't have any content for UVa online judge yet.");
                    }
                },
                function () { // error
                    AlertService.fireWarning("The site is having trouble loading solutions for UVa. Please try again later :p");
                });
        };

        $scope.populatePermission = function () {
            if (AuthService.isLoggedIn()) {
                $scope.solutions.forEach(function (solution) {
                    SolutionPermission.canDelete({
                        id: solution.id,
                        contest: 'UVA_ONLINE_JUDGE'
                    }).$promise
                        .then(function (response) {
                            solution.canDelete = response.isAllowed;
                        })
                });
            }
        };

        $scope.pickSolution = function (solution) {
            UVa.getContent({id: solution.id},
                function (response) {
                    $scope.pickedSolution = {
                        language: solution.language,
                        content: response.content,
                        link: "www.codingyard.com/uva/view?id=" + solution.id
                    };
                    angular.extend($scope.pickedSolution, solution);
                    $scope.modeChanged();
                },
                function () {
                    AlertService.fireWarning("Oops... we had trouble loading the content for the chosen solution. Please try later.");
                    $scope.pickedSolution = null;
                }
            );
        };

        $scope.getSolutions();

        if (!AuthService.isLoggedIn()) {
            $scope.$on(AUTH_EVENTS.gotBasicUserInfo, $scope.populatePermission);
        }

        // show picked solution if a user specified the id of solution to display.
        if ($scope.pickedId) {
            UVa.get({id: $scope.pickedId})
                .$promise
                .then(function (solution) {
                    $scope.pickSolution(solution);
                });
        }
    });
})();