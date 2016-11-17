(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('PriceInfo', PriceInfo);

    PriceInfo.$inject = ['$resource', 'DateUtils'];

    function PriceInfo ($resource, DateUtils) {
        var resourceUrl =  'api/price-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fromValidDate = DateUtils.convertDateTimeFromServer(data.fromValidDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
