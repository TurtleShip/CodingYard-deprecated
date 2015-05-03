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
            .when('/user/:userId', {
                templateUrl: '/app/components/users/view-user.html',
                controller: 'UserController'
            })
            .when('/solution/topcoder/upload', {
                templateUrl: '/app/components/solutions/topcoder/upload.html',
                controller: 'TopCoderUploadController'
            })
            .when('/solution/topcoder/view', {
                templateUrl: '/app/components/solutions/topcoder/view.html',
                controller: 'TopCoderViewController'
            })

            .otherwise({
                templateUrl: '/404.html'
            });
    });
})();
