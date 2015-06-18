'use strict';

(function () {
    app.controller('TopCoderUploadController', function ($scope, $log, TopCoder, AceEditor) {
        $scope.solution = {
            division: "DIV2",
            difficulty: "EASY",
            problem_number: 1,
            language: "JAVA",
            content: "Hello world! :)"
        };
        $scope.uploadSuccess = null;
        $scope.uploadError = null;

        $scope.languages = {
            "JAVA": "Java",
            "C": "ANSI C",
            "CPP": "cpp",
            "PYTHON": "Python",
            "RUBY": "Ruby",
            "OTHER": "Other"
        };

        $scope.uploadSolution = function (solutionToUpload) {

            TopCoder.upload({}, solutionToUpload, function () {
                    $scope.uploadSuccess = "Upload was successful :)";
                },
                function (response) {
                    $scope.uploadError = response.data;
                })
        };

        $scope.aceOption = {
            mode: AceEditor.getMode($scope.solution.language),
            useWrapMode: true,
            showGutter: true,
            theme: 'twilight',
            onLoad: function (_ace) {

                $scope.modeChanged = function () {
                    _ace.getSession().setMode("ace/mode/" + AceEditor.getMode($scope.solution.language));
                };

            }
        };
    });
})();