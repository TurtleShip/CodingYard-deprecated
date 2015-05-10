(function () {
    app.controller('TopCoderViewController', function ($scope, $log, TopCoder) {
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
                                $scope.message = null;
                                $scope.content = "Implement me to get content";

                                break;
                            default:
                                $scope.message = "There are " + $scope.solutions.length + " solutions matching the given solution.";
                                $scope.content = null;
                                $scope.populateAvailable(response);
                        }

                        if ($scope.solutions.length == 1) {
                            $scope.content = "Implement me to get content";
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
