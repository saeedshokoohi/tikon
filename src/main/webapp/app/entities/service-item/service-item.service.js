(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('ServiceItem', ServiceItem);

    ServiceItem.$inject = ['$resource'];

    function ServiceItem ($resource) {
        var resourceUrl =  'api/service-items/:id';

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
