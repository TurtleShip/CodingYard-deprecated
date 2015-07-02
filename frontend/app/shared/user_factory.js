'user strict';

// Provide a factory to access User resource endpoint.
(function () {
    app.factory('User', function UserFactory($log, $resource, TransformRequest) {
        return $resource('/api/user/:id', {}, {
            getAllUsers: {
                url: '/api/user/all',
                method: 'GET',
                isArray: true
            },
            getSolutions: {
                url: '/api/user/:id/solutions',
                method: 'GET',
                isArray: true
            },
            getMyInfo: {
                url: '/api/user/me',
                method: 'GET'
            },
            changeMyInfo: {
                url: '/api/user/me/edit',
                method: 'PUT'
            },
            createUser: {
                method: 'POST',
                transformRequest: TransformRequest
            },
            deleteUser: {
                method: 'DELETE'
            },
            refreshToken: {
                url: '/api/user/token/refresh',
                method: 'POST'
            },
            editRole: {
                url: '/api/user/edit/role',
                method: 'PUT'
            },

            editFirstName: {
                url: '/api/user/edit/firstname',
                method: 'PUT',
                transformRequest: TransformRequest
            },

            editLastName: {
                url: '/api/user/edit/lastname',
                method: 'PUT',
                transformRequest: TransformRequest
            },

            editEmail: {
                url: '/api/user/edit/email',
                method: 'PUT',
                transformRequest: TransformRequest
            }
        });
    });
})();
