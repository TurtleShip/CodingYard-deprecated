(function () {
    app.config(function ($stateProvider, $urlRouterProvider, $locationProvider) {
        $locationProvider.html5Mode(true);
        $locationProvider.hashPrefix('!');

        $stateProvider
            .state('home', {
                url: '/',
                templateUrl: '/app/components/home/home.html'
            })
            .state('signUp', {
                url: '/signup',
                templateUrl: '/app/components/signup/index.html',
                controller: 'SignUpController'
            })
            .state('userView', {
                url: '/user/:userId',
                templateUrl: '/app/components/users/index.html',
                controller: 'UserController'
            });


        $urlRouterProvider.when('', '/')
            .otherwise('/');
    });
})();
