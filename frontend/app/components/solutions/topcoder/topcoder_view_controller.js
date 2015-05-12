(function () {
    app.controller('TopCoderViewController', function ($scope, $log, TopCoder, AceEditor) {
            $scope.solutions = null;
            $scope.content = null;
            $scope.message = null;
            $scope.criteria = {
                division: null,
                difficulty: null,
                problem_number: null,
                language: null,
                author_username: null
            };

            $scope.available = {
                divisions: {},
                difficulties: {},
                problem_numbers: {},
                languages: {},
                author_usernames: {}
            };

            $scope.populateAvailable = function (solutions) {

                // initialize available

                $scope.available = {
                    divisions: {},
                    difficulties: {},
                    problem_numbers: {},
                    languages: {},
                    author_usernames: {}
                };

                solutions.forEach(function (solution) {
                    if (!(solution.division in $scope.available.divisions)) {
                        $scope.available.divisions[solution.division] = true;
                    }
                    if (!(solution.difficulty in $scope.available.difficulties)) {
                        $scope.available.difficulties[solution.difficulty] = true;
                    }
                    if (!(solution.problem_number in $scope.available.problem_numbers)) {
                        $scope.available.problem_numbers[solution.problem_number] = true;
                    }

                    if (!(solution.language in $scope.available.languages)) {
                        $scope.available.languages[solution.language] = true;
                    }
                    if (!(solution.author.username in $scope.available.author_usernames)) {
                        $scope.available.author_usernames[solution.author.username] = true;
                    }
                });

                $scope.available.divisions = Object.keys($scope.available.divisions);
                $scope.available.difficulties = Object.keys($scope.available.difficulties);
                $scope.available.problem_numbers = Object.keys($scope.available.problem_numbers);
                $scope.available.languages = Object.keys($scope.available.languages);
                $scope.available.author_usernames = Object.keys($scope.available.author_usernames);
            };

            $scope.aceOption = {
                useWrapMode: true,
                showGutter: true,
                theme: 'twilight',
                onLoad: function (_ace) {
                    $scope.modeChanged = function () {
                        $log.info("################# Changing mode");
                        _ace.getSession().setMode("ace/mode/" + AceEditor.getMode($scope.language));
                    };
                }
            };

            $scope.getSolutions = function () {
                TopCoder.findAll($scope.criteria,
                    function (response) { // success
                        $scope.solutions = response;
                        switch ($scope.solutions.length) {
                            case 0:
                                $scope.message = "There is no solution matching the given criteria.";
                                $scope.content = null;
                                break;
                            case 1:
                                var solution = response[0];
                                TopCoder.getContent({id: solution["solution_id"]},
                                    function (response) {
                                        $scope.message = "This is the solution matching your criteria.";
                                        $scope.content = AceEditor.parseLines(response);
                                    },
                                    function () {
                                        $scope.message = "Found a solution matching your criteria, but had trouble loading it :(.";
                                        $scope.content = null;
                                    }
                                );

                                break;
                            default:
                                $scope.message = "There are " + $scope.solutions.length + " solutions matching the given solution.";
                                $scope.content = null;
                                $scope.populateAvailable(response);
                        }
                    },
                    function (response) { // error
                        $scope.message = response;
                    });
            };

            $scope.getSolutions();
        }
    )
    ;
})();
