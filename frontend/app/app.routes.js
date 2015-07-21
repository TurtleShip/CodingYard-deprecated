'use strict';

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
                templateUrl: '/app/components/signup/signup.html',
                controller: 'SignUpController'
            })
            .state('userView', {
                url: '/user/detail/:userId',
                templateUrl: '/app/components/users/index.html',
                controller: 'UserController'
            })
            .state('userListView', {
                url: '/user/list',
                templateUrl: '/app/components/users/list.html',
                controller: 'UserListController'
            })
            .state('topcoderView', {
                url: '/topcoder/view',
                templateUrl: '/app/components/topcoder/view.html',
                controller: 'TopCoderViewController'
            })
            .state('topcoderUpload', {
                url: '/topcoder/upload',
                templateUrl: '/app/components/topcoder/upload.html',
                controller: 'TopCoderUploadController'
            });

        $urlRouterProvider.when('', '/')
            .otherwise('/');
    });
})();
