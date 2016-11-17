(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('ServiceCapacityInfo', ServiceCapacityInfo);

    ServiceCapacityInfo.$inject = ['$resource'];

    function ServiceCapacityInfo ($resource) {
        var resourceUrl =  'api/service-capacity-infos/:id';

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
