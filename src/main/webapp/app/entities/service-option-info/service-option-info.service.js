(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('ServiceOptionInfo', ServiceOptionInfo);

    ServiceOptionInfo.$inject = ['$resource'];

    function ServiceOptionInfo ($resource) {
        var resourceUrl =  'api/service-option-infos/:id';

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
