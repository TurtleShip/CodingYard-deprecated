'use strict';

(function () {
    app.controller('UserListController', function ($scope, $q, $log, $modal, User, UserPermission, AuthService) {

        $scope.displayedUsers = null;
        $scope.selectedUser = null;
        var loggedIn = AuthService.isLoggedIn();

        User.getAllUsers({}, function (users) {
            $scope.users = users;

            if (loggedIn) {
                $scope.users.forEach(function (user) {

                    UserPermission.canEdit({id: user.id}, function (permission) {
                        user.canEdit = permission.isAllowed;
                    });
                    UserPermission.canDelete({id: user.id}, function (permission) {
                        user.canDelete = permission.isAllowed;
                    });
                });
            }
        });


        $scope.paginationSetting = {
            itemPerPage: 5,
            displayedPages: 10
        };

        $scope.supported = {
            roles: [
                {
                    key: 'Any',
                    value: ''
                },
                {
                    key: 'Global admin',
                    value: 'GLOBAL_ADMIN'
                },
                {
                    key: 'Admin',
                    value: 'ADMIN'
                },
                {
                    key: 'Member',
                    value: 'MEMBER'
                },
                {
                    key: 'Guest',
                    value: 'GUEST'
                }
            ]
        };

        $scope.deleteUser = function (user) {
            User.deleteUser({id: user.id},
                function () {
                    var index = $scope.users.indexOf(user);
                    if (index !== -1) {
                        $scope.users.splice(index, 1);
                    }
                });
        };

        $scope.userToEdit = null;

        $scope.openEdit = function (user) {
            $scope.userToEdit = user;
            $modal.open({
                animation: true,
                templateUrl: '/app/components/users/popup/edit.html?bust=' + Math.random().toString(36).slice(2),
                controller: 'UserEditConfirmController',
                resolve: {
                    userToEdit: function () {
                        return $scope.userToEdit;
                    }
                }
            });


        };

        $scope.userToDelete = null;

        $scope.openConfirm = function (user) {

            $scope.userToDelete = user;

            var modalInstance = $modal.open({
                animation: true,
                templateUrl: '/app/components/users/popup/delete.html?bust=' + Math.random().toString(36).slice(2),
                controller: 'UserDeleteConfirmController',
                size: 'sm',
                resolve: {
                    userToDelete: function () {
                        return $scope.userToDelete;
                    }
                }
            });

            modalInstance.result.then($scope.deleteUser);
        }
    });
})();