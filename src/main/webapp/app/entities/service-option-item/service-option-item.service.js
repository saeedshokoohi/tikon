(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('ServiceOptionItem', ServiceOptionItem);

    ServiceOptionItem.$inject = ['$resource'];

    function ServiceOptionItem ($resource) {
        var resourceUrl =  'api/service-option-items/:id';

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
