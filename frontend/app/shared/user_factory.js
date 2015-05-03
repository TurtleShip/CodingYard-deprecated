'user strict';

// Provides CRUD methods on users.
(function () {
    app.factory('User', function UserFactory($resource) {
        return $resource('/api/user/:id', {}, {});
    });
})();
