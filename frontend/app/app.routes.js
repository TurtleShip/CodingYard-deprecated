(function () {
    app.config(function ($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true);
        $locationProvider.hashPrefix('!');

        $routeProvider
            .when('/', {
                templateUrl: '/app/components/home/home.html'
            })
            .when('/view-solutions', {
                templateUrl: '/app/components/view-solutions/view-solutions.html'
            })

            .otherwise({
                templateUrl: '/404.html'
            });
    });
})();
