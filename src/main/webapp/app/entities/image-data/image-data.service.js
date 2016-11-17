(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('ImageData', ImageData);

    ImageData.$inject = ['$resource'];

    function ImageData ($resource) {
        var resourceUrl =  'api/image-data/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
