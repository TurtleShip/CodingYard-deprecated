'user strict';

(function () {
    app.factory('TopCoder', function TopCoderFactory($resource, TransformRequest) {

        return $resource('/api/solution/topcoder/:id', {}, {
            upload: {
                method: 'POST',
                transformRequest: TransformRequest
            },
            getContent: {
                method: 'GET',
                url: '/api/solution/topcoder/:id',
                params: {
                    include_content: true
                },
                isArray: true
            },
            findAll: {
                method: 'GET',
                url: '/api/solution/topcoder',
                isArray: true
            }
        });
    });
})();