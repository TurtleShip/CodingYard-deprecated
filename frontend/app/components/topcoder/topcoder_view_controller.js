(function () {
    app.controller('TopCoderViewController', function ($scope, $log, TopCoder, AceEditor) {
            $scope.solutions = null;
            $scope.pickedSolution = null;
            $scope.message = null;
            $scope.error = null;

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
                TopCoder.findAll($scope.criteria,
                    function (response) { // success
                        $scope.solutions = response;
                        switch ($scope.solutions.length) {
                            case 0:
                                $scope.message = "Sorry. We don't have any content for TopCoder yet.";
                                break;
                            default:
                                $scope.message = "There are " + $scope.solutions.length + " solutions matching the given solution.";
                                $scope.populateAvailable(response);
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
