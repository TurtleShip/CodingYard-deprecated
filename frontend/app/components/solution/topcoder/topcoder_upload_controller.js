'use strict';

(function () {
    app.controller('TopCoderUploadController', function ($scope, $log, TopCoder, AceEditor,
                                                         SharedData, AuthService, AlertService, AUTH_EVENTS) {

        $scope.canSubmit = false;
        $scope.getPermission = function () {
            $scope.currentUser = SharedData.getSharedData().currentUser;
            $scope.canSubmit = AuthService.isAuthorized(['GLOBAL_ADMIN', 'ADMIN', 'MEMBER'], $scope.currentUser);
        };

        $scope.getPermission();
        $scope.$on(AUTH_EVENTS.gotBasicUserInfo, $scope.getPermission);

        $scope.solution = {
            division: "DIV1",
            difficulty: "EASY",
            problem_number: 1,
            language: "JAVA",
            content: "// Enter your solution here please ;)"
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
                    AlertService.fireSuccess("Upload was successful :)");
                },
                function () {
                    AlertService.fireWarning("Sorry, we couldn't upload your solution.");
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