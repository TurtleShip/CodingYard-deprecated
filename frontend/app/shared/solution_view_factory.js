'use strict';

/*
    I am shoving logics that are common in displaying solutions here.
    I probably need to factor these out in more sensible way later on.
 */
(function() {
    app.factory('SolutionView', function SolutionViewFactory() {
       var solutionView = {};

        solutionView.paginationSetting = {
            itemPerPage: 5,
            displayedPages: 10
        };

        return solutionView;
    });
})();