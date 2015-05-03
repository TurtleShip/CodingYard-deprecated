(function () {
    app.controller('TopCoderViewController', function ($scope, $log, TopCoder) {
        $scope.solution = null;
        $scope.content = null;


        $scope.getSolution = function (id) {
            TopCoder.get({id: id}, function (data) {
                $scope.solution = data;
                TopCoder.getContent({id: id}, function (data) {
                    $scope.content = data;
                })
            });
        };
    });
})();
