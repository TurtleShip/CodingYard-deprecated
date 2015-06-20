'use strict';

(function () {
    app.controller('TopCoderViewController', function ($scope, $log, $modal, TopCoder, AceEditor, SolutionPermission) {
            $scope.solutions = null;
            $scope.displayedSolutions = null;
            $scope.pickedSolution = null;
            $scope.message = null;
            $scope.error = null;

            $scope.supported = {
                divisions: {
                    'Any': '',
                    'Div 1': 'DIV1',
                    'Div 2': 'DIV2'
                },
                difficulties: {
                    Any: '',
                    easy: 'EASY',
                    medium: 'MEDIUM',
                    hard: 'HARD'
                },
                languages: {
                    Any: '',
                    Java: 'JAVA',
                    Cpp: 'CPP',
                    C: 'C',
                    Python: 'PYTHON',
                    Ruby: 'RUBY',
                    Other: 'OTHER'
                }
            };

            $scope.criteria = {
                division: '',
                difficulty: '',
                problem_number: '',
                language: '',
                author_username: ''

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
                }
            };

            $scope.getSolutions = function () {
                TopCoder.findAll({},
                    function (response) { // success
                        $scope.solutions = response;
                        $scope.solutions.forEach(function (solution) {
                            solution.canDelete = false;
                            solution.author_name = solution.author.username;
                            SolutionPermission.canDelete({
                                    id: solution.id,
                                    contest: 'TOP_CODER'
                                }, function () {
                                    solution.canDelete = true;
                                }
                            );
                        });
                        switch ($scope.solutions.length) {
                            case 0:
                                $scope.message = "Sorry. We don't have any content for TopCoder yet.";
                                break;
                            default:
                                $scope.message = "There are " + $scope.solutions.length + " solutions matching the given solution.";
                        }
                    },
                    function () { // error
                        $scope.error = "The site is having trouble loading solutions for TopCoder. Please try again later :p";
                    });
            };

            $scope.pickSolution = function (solution) {
                $log.log("picked a solution");
                TopCoder.getContent({id: solution.id},
                    function (response) {
                        $scope.pickedSolution = {
                            language: solution.language,
                            content: response.content
                        };
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
        }
    );
})();
