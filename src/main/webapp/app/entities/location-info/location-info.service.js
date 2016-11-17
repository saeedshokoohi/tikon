(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('LocationInfo', LocationInfo);

    LocationInfo.$inject = ['$resource'];

    function LocationInfo ($resource) {
        var resourceUrl =  'api/location-infos/:id';

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
