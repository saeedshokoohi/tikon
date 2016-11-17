(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('CompanyManager', CompanyManager);

    CompanyManager.$inject = ['$resource'];

    function CompanyManager ($resource) {
        var resourceUrl =  'api/company-managers/:id';

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
