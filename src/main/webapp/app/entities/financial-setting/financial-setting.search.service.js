(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('FinancialSettingSearch', FinancialSettingSearch);

    FinancialSettingSearch.$inject = ['$resource'];

    function FinancialSettingSearch($resource) {
        var resourceUrl =  'api/_search/financial-settings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
