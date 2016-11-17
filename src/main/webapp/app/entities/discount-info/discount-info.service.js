(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('DiscountInfo', DiscountInfo);

    DiscountInfo.$inject = ['$resource'];

    function DiscountInfo ($resource) {
        var resourceUrl =  'api/discount-infos/:id';

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
