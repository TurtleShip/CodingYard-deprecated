'use strict';

(function () {
    app.factory('UserPermission', function UserPermissionFactory($resource, $log) {
        return $resource('', {}, {
            canDelete: {
                url: 'api/permission/user/:id/delete',
                method: 'GET',
                transformResponse: function (permission) {
                    return {
                        isAllowed: permission === 'true'
                    }
                }
            },
            canEdit: {
                url: 'api/permission/user/:id/edit',
                method: 'GET',
                transformResponse: function (permission) {
                    return {
                        isAllowed: permission === 'true'
                    }
                }
            },
            canEditEmail: {
                url: 'api/permission/user/:id/edit/email',
                method: 'GET',
                transformResponse: function (permission) {
                    return {
                        isAllowed: permission === 'true'
                    }
                }
            },
            canEditFirstName: {
                url: 'api/permission/user/:id/edit/firstname',
                method: 'GET',
                transformResponse: function (permission) {
                    return {
                        isAllowed: permission === 'true'
                    }
                }
            },
            canEditLastName: {
                url: 'api/permission/user/:id/edit/lastname',
                method: 'GET',
                transformResponse: function (permission) {
                    return {
                        isAllowed: permission === 'true'
                    }
                }
            },
            canEditPassword: {
                url: 'api/permission/user/:id/edit/password',
                method: 'GET',
                transformResponse: function (permission) {
                    return {
                        isAllowed: permission === 'true'
                    }
                }
            },
            getEditableRoles: {
                url: 'api/permission/user/:id/edit/role',
                method: 'GET',
                isArray: true
            }
        });

    });
})();