'user strict';

// Provides CRUD methods on users.
(function () {
    app.factory('User', function UserFactory($resource, TransformRequest) {
        return $resource('/api/user/:id', {}, {
            createUser: {
                method: 'POST',
                transformRequest: TransformRequest
            }
        });
    });
})();
