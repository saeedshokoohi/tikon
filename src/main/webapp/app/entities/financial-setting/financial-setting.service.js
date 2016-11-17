(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('FinancialSetting', FinancialSetting);

    FinancialSetting.$inject = ['$resource'];

    function FinancialSetting ($resource) {
        var resourceUrl =  'api/financial-settings/:id';

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
