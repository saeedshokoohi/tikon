(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('CustomerComment', CustomerComment);

    CustomerComment.$inject = ['$resource', 'DateUtils'];

    function CustomerComment ($resource, DateUtils) {
        var resourceUrl =  'api/customer-comments/:id';

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
