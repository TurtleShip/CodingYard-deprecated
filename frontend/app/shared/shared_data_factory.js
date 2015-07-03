'use strict';


(function () {
    app.factory('SharedData', function SharedDataFactory() {
        var sharedData = {
           currentUser: null
        };

        return {
            getSharedData: function () {
                return sharedData;
            }
        }
    });
})();