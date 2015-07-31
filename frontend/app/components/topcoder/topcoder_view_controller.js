'use strict';

(function () {
    app.controller('TopCoderViewController', function ($scope, $log, $modal, $filter, $timeout,
                                                       TopCoder, AceEditor, SolutionPermission,
                                                       AuthService, AUTH_EVENTS, $stateParams) {

            $scope.paginationSetting = {
                itemPerPage: 5,
                displayedPages: 10
            };

            $scope.alerts = [];
            $scope.pickedId = parseInt($stateParams.id);

            $scope.addAlert = function (isWarning, msg) {
                $scope.alerts.push({
                    type: isWarning ? 'danger' : 'success',
                    msg: msg
                });
            };

            $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
            };

            $scope.solutions = null;
            $scope.displayedSolutions = null;
            $scope.pickedSolution = null;

            $scope.supported = {
                divisions: [
                    {key: 'Any', value: ''},
                    {key: 'Div 1', value: 'DIV1'},
                    {key: 'Div 2', value: 'DIV2'}
                ],
                difficulties: [
                    {key: 'Any', value: ''},
                    {key: 'easy', value: 'EASY'},
                    {key: 'medium', value: 'MEDIUM'},
                    {key: 'hard', value: 'HARD'}
                ],
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
                TopCoder.deleteSolution({id: solution.id},
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
                    templateUrl: '/app/components/topcoder/delete_confirm.html?bust=' + Math.random().toString(36).slice(2),
                    controller: 'TopCoderDeleteConfirmController',
                    size: 'sm',
                    resolve: {
                        solutionToDelete: function () {
                            return $scope.solutionToDelete;
                        }
                    }
                });

                modalInstance.result.then($scope.deleteSolution);
            };

            $scope.aceOption = {
                mode: "text",
                useWrapMode: true,
                showGutter: true,
                theme: 'twilight',
                onLoad: function (_ace) {
                    $scope.modeChanged = function () {
                        if ($scope.pickedSolution) {
                            _ace.getSession().setMode("ace/mode/" + AceEditor.getMode($scope.pickedSolution.language));
                        }
                    };

                    _ace.$blockScrolling = Infinity;
                }
            };

            $scope.getSolutions = function () {
                TopCoder.findAll({},
                    function (response) { // success
                        $scope.solutions = response;
                        $scope.solutions.forEach(function (solution) {

                            if (AuthService.isAuthenticated()) {
                                SolutionPermission.canDelete({
                                    id: solution.id,
                                    contest: 'TOP_CODER'
                                }).$promise
                                    .then(function () {
                                        solution.canDelete = true;
                                    })
                            }

                        });
                        if ($scope.solutions.length == 0) {
                            $scope.addAlert(false, "Sorry. We don't have any content for TopCoder yet.");
                        }
                    },
                    function () { // error
                        $scope.addAlert(true, "The site is having trouble loading solutions for TopCoder. Please try again later :p");
                    });
            };

            $scope.pickSolution = function (solution) {
                TopCoder.getContent({id: solution.id},
                    function (response) {
                        $scope.pickedSolution = {
                            language: solution.language,
                            content: response.content
                        };
                        angular.extend($scope.pickedSolution, solution);

                        $scope.error = null;
                        $scope.modeChanged();
                    },
                    function () {
                        $scope.error = "Oops... we had trouble loading the content for the chosen solution. Please try later.";
                        $scope.pickedSolution = null;
                    }
                );
            };

            $scope.getSolutions();
            $scope.$on(AUTH_EVENTS.gotBasicUserInfo, $scope.getSolutions());

            // show picked solution if a user specified the id of solution to display.
            if ($scope.pickedId) {
                TopCoder.get({id: $scope.pickedId})
                    .$promise
                    .then(function (solution) {
                        $scope.pickSolution(solution);
                    });
            }
        }
    );
})();
