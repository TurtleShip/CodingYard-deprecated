'use strict';

(function () {
    app.factory('UVa', function TopCoderFactory($resource, TransformRequest) {

        return $resource('/api/solution/uva/:id', {}, {
            upload: {
                method: 'POST',
                transformRequest: TransformRequest
            },
            getContent: {
                method: 'GET',
                url: '/api/solution/uva/:id/content',
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
                url: '/api/solution/uva',
                isArray: true
            },
            deleteSolution: {
                method: 'DELETE',
                url: '/api/solution/uva/:id'
            },
            editProblemName: {
                url: '/api/solution/uva/edit/name',
                method: 'PUT',
                transformRequest: TransformRequest
            },
            editProblemLink: {
                url: '/api/solution/uva/edit/link',
                method: 'PUT',
                transformRequest: TransformRequest
            }
        });
    });
})();