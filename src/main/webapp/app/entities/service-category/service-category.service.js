(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('ServiceCategory', ServiceCategory);

    ServiceCategory.$inject = ['$resource'];

    function ServiceCategory ($resource) {
        var resourceUrl =  'api/service-categories/:id';

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
