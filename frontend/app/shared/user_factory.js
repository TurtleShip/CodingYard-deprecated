'user strict';

// Provide a factory to access User resource endpoint.
(function () {
    app.factory('User', function UserFactory($log, $resource, TransformRequest) {
        return $resource('/api/user/:id', {}, {
            getSolutions: {
                url: '/api/user/:id/solutions',
                method: 'GET',
                isArray: true
            },
            getMyInfo: {
                url: '/api/user/me',
                method: 'GET'
            },
            createUser: {
                method: 'POST',
                transformRequest: TransformRequest
            },
            refreshToken: {
                url: '/api/user/token/refresh',
                method: 'POST'
            },
            changeRole: {
                url: '/api/user/role',
                method: 'PUT'
            }
        });
    });
})();
