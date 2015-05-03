(function () {
    app.controller('TopCoderUploadController', function ($scope, $log, TopCoder) {
        $scope.solution = {
            division: "DIV2",
            difficulty: "EASY",
            problem_number: 1,
            language: "JAVA",
            content: "Hello world! :)"
        };
        $scope.uploadSuccess = null;
        $scope.uploadError = null;


        $scope.uploadSolution = function (solutionToUpload) {

            TopCoder.upload({}, solutionToUpload, function () {
                    $scope.uploadSuccess = "Upload was successful :)";
                },
                function (response) {
                    $scope.uploadError = response.data;
                })
        };
    });
})();