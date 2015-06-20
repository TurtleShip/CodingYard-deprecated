'use strict';

(function () {
    app.controller('TopCoderUploadController', function ($scope, $log, TopCoder, AceEditor) {

        $scope.alerts = [];

        $scope.addAlert = function (isWarning, msg) {
            $scope.alerts.push({
                type: isWarning ? 'danger' : 'success',
                msg: msg
            });
        };

        $scope.closeAlert = function (index) {
            $scope.alerts.splice(index, 1);
        };

        $scope.solution = {
            division: "DIV1",
            difficulty: "EASY",
            problem_number: 1,
            language: "JAVA",
            content: "Hello world! :)"
        };

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
                    $scope.addAlert(false, "Upload was successful :)");
                },
                function () {
                    $scope.addAlert(true, "Sorry, we couldn't upload your solution.");
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

                _ace.$blockScrolling = Infinity;
            }
        };
    });
})();