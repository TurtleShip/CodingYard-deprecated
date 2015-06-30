'use strict';

(function () {
    app.controller('UserController', function ($stateParams, $scope, $rootScope, $log, $q,
                                               User, UserPermission, USER_ROLES,
                                               AUTH_EVENTS,
                                               SharedData, AuthService) {

        $scope.alerts = [];

        $scope.addAlert = function (isWarning, msg) {
            $scope.alerts.push({
                type: isWarning ? 'danger' : 'success',
                msg: msg
            });
        };

        $scope.closeAlert = function (index) {
            $scope.alerts.splice(index, 1);
        };

        $scope.isAuthorized = AuthService.isAuthorized;

        $scope.userRoles = USER_ROLES;
        $scope.errorMessage = null;

        $scope.sharedData = SharedData.getSharedData();

        $scope.viewedUser = null;

        $scope.canEdit = {
            userName: false,
            firstName: false,
            lastName: false,
            email: false,
            role: []
        };

        $scope.isArray = angular.isArray;

        $scope.editFirstName = function () {
            var deferred = $q.defer();

            User.editFirstName({
                newFirstName: $scope.viewedUser.first_name,
                id: $scope.viewedUser.id
            }, function () {
                $scope.addAlert(false, "Firstname has successfully been changed to " + $scope.viewedUser.first_name);
                deferred.resolve();
            }, function () {
                // TODO: Display message differently depend on the error code ( ex> 401 vs 500 )
                $scope.addAlert(true, "We couldn't update the first name.");
                deferred.resolve("Error editing first name.");
            });

            return deferred.promise;
        };

        $scope.editLastName = function () {
            var deferred = $q.defer();

            User.editLastName({
                    newLastName: $scope.viewedUser.last_name,
                    id: $scope.viewedUser.id
                }, function success() {
                    $scope.addAlert(false, "Last name has successfully been changed to " + $scope.viewedUser.last_name);
                    deferred.resolve();
                }, function error() {
                    $scope.addAlert(true, "We couldn't update last name.");
                    deferred.resolve("Error editing last name.");
                }
            );

            return deferred.promise;
        };

        $scope.editEmail = function () {
            var deferred = $q.defer();

            User.editEmail({
                newEmail: $scope.viewedUser.email,
                id: $scope.viewedUser.id
            }, function success() {
                $scope.addAlert(false, "Email has successfully been changed to " + $scope.viewedUser.email);
                deferred.resolve();
            }, function error() {
                $scope.addAlert(true, "We couldn't update email.");
                deferred.resolve("Error editing email.");
            });

            return deferred.promise;
        };

        $scope.editRole = function () {
            var deferred = $q.defer();

            User.editRole({
                new_role: $scope.viewedUser.role,
                user_id: $scope.viewedUser.id
            }, function success() {
                $scope.addAlert(false, "Role has successfully been changed to " + $scope.viewedUser.role);
                deferred.resolve();
            }, function error() {
                $scope.addAlert(true, "We couldn't update role.");
                deferred.resolve("WTF");
            });

            return deferred.promise;
        };

        $scope.editFormFields = [
            {
                label: 'Username',
                editField: 'username',
                canEditKey: 'userName',
                onAfterSave: null
            },
            {
                label: 'First name',
                editField: 'first_name',
                canEditKey: 'firstName',
                onAfterSave: $scope.editFirstName
            },
            {
                label: 'Last name',
                editField: 'last_name',
                canEditKey: 'lastName',
                onAfterSave: $scope.editLastName
            },
            {
                label: 'Email',
                editField: 'email',
                canEditKey: 'email',
                onAfterSave: $scope.editEmail
            },
            {
                label: 'Role',
                editField: 'role',
                canEditKey: 'role',
                onAfterSave: $scope.editRole
            }
        ];

        var populatePermission = function () {
            var viewedUserId = $stateParams.userId;
            UserPermission.canEditFirstName({id: viewedUserId},
                function (data) {
                    $log.log("Edit access for firstname is approved");
                    $scope.canEdit.firstName = data.isAllowed;
                }
            );

            UserPermission.canEditLastName({id: viewedUserId},
                function (data) {
                    $scope.canEdit.lastName = data.isAllowed;
                }
            );

            UserPermission.canEditEmail({id: viewedUserId},
                function (data) {
                    $scope.canEdit.email = data.isAllowed;
                }
            );

            UserPermission.getEditableRoles({id: viewedUserId},
                function (data) {
                    $log.log("Allowed role : " + data);
                    $scope.canEdit.role = data;
                }
            );
        };

        if (!!$scope.sharedData.currentUser) {
            populatePermission();
        }
        $scope.$on(AUTH_EVENTS.gotBasicUserInfo, populatePermission);

        if (!!$scope.sharedData.currentUser && $scope.sharedData.currentUser.id === $stateParams.userId) {
            $scope.viewedUser = $scope.sharedData.currentUser;

        } else {
            User.get({id: $stateParams.userId},
                function (data) {
                    $scope.viewedUser = data;
                },
                function () {
                    $scope.errorMessage = "Sorry, we are having trouble loading user " + $stateParams.userId;
                }
            );
        }
    })
})();
