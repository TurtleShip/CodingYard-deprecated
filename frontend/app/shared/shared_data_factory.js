'use strict';

// Responsible for retrieving/updating/saving current user's information.
(function () {
    app.factory('SharedData', function SharedDataFactory() {
        var user = null;
        return {
            getCurrentUser: function () {
                return user;
            },
            setCurrentUser: function (newUser) {
                user = newUser;
            }
        }
    });
})();