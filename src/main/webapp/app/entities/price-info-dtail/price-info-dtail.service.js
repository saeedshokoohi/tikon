(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('PriceInfoDtail', PriceInfoDtail);

    PriceInfoDtail.$inject = ['$resource'];

    function PriceInfoDtail ($resource) {
        var resourceUrl =  'api/price-info-dtails/:id';
        debugger;
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
