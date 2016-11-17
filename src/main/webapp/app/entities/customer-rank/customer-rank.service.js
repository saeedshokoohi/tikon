(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('CustomerRank', CustomerRank);

    CustomerRank.$inject = ['$resource', 'DateUtils'];

    function CustomerRank ($resource, DateUtils) {
        var resourceUrl =  'api/customer-ranks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createDate = DateUtils.convertDateTimeFromServer(data.createDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
