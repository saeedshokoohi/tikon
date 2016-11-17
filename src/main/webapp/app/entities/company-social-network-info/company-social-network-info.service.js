(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('CompanySocialNetworkInfo', CompanySocialNetworkInfo);

    CompanySocialNetworkInfo.$inject = ['$resource'];

    function CompanySocialNetworkInfo ($resource) {
        var resourceUrl =  'api/company-social-network-infos/:id';

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
