(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('CapacityException', CapacityException);

    CapacityException.$inject = ['$resource', 'DateUtils'];

    function CapacityException ($resource, DateUtils) {
        var resourceUrl =  'api/capacity-exceptions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.reserveTime = DateUtils.convertDateTimeFromServer(data.reserveTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
