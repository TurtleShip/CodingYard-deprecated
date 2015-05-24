'use strict';

(function () {
    // Provides method to store and load session info to client-side (a.k.a browser )
    app.factory('SessionStorage', function ($window) {
        return {
            set: function (key, value) {
                $window.localStorage[key] = value;
            },
            get: function (key, defaultValue) {
                return $window.localStorage[key] || defaultValue;
            },
            setObject: function (key, value) {
                $window.localStorage[key] = JSON.stringify(value);
            },
            getObject: function (key) {
                return JSON.parse($window.localStorage[key] || '{}');
            },
            remove: function(key) {
                $window.localStorage.removeItem(key);
            }
        }
    });

    app.service('Session', function ($log) {
        this.create = function (token, user) {
            $log.log("token : " + token);
            $log.log("user  : " + user);
            this.token = token;
            this.user = user;
        };

        this.destroy = function () {
            this.token = null;
            this.user = null;
        }
    });
})();
