'use strict';

(function () {
    app.factory('TopCoder', function TopCoderFactory($resource, TransformRequest) {

        return $resource('/api/solution/topcoder/:id', {}, {
            upload: {
                method: 'POST',
                transformRequest: TransformRequest
            },
            getContent: {
                method: 'GET',
                url: '/api/solution/topcoder/:id/content',
                transformResponse: function (data) {
                    /* need to transform data because return type is plain-text ( string ),
                     and $resource cannot handle String return type.
                     Reference: http://stackoverflow.com/questions/24876593/resource-query-return-split-strings-array-of-char-instead-of-a-string
                     */
                    return {
                        content: data
                    }
                }
            },
            findAll: {
                method: 'GET',
                url: '/api/solution/topcoder',
                isArray: true
            }
        });
    });
})();