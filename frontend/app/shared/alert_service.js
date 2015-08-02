'use strict';

(function() {
    app.factory('AlertService', function($rootScope, ALERT_EVENTS) {
        return {
            fireWarning: function fireWarning(msg) {
                $rootScope.$broadcast(ALERT_EVENTS.alertFired, true, msg);
            },
            fireSuccess: function fireSuccess(msg) {
                $rootScope.$broadcast(ALERT_EVENTS.alertFired, false, msg);
            }
        };
    });
})();