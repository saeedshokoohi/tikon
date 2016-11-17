(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('InvoiceInfo', InvoiceInfo);

    InvoiceInfo.$inject = ['$resource', 'DateUtils'];

    function InvoiceInfo ($resource, DateUtils) {
        var resourceUrl =  'api/invoice-infos/:id';

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
