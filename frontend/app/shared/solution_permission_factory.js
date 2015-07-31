'use strict';

(function () {
    app.factory('SolutionPermission', function TopCoderFactory($resource) {

        return $resource('', {}, {
            canCreate: {
                url: '/api/permission/solution/create',
                method: 'GET',
                transformResponse: function (permission) {
                    return {
                        isAllowed: permission === 'true'
                    }
                }
            },
            canDelete: {
                url: '/api/permission/solution/delete/:contest/:id',
                method: 'GET',
                transformResponse: function (permission) {
                    return {
                        isAllowed: permission === 'true'
                    }
                }
            },
            canEdit: {
                url: '/api/permission/solution/edit/:contest/:id',
                method: 'GET',
                transformResponse: function (permission) {
                    return {
                        isAllowed: permission === 'true'
                    }
                }
            }
        });
    });
})();