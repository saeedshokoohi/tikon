(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('CompanyManagerSearch', CompanyManagerSearch);

    CompanyManagerSearch.$inject = ['$resource'];

    function CompanyManagerSearch($resource) {
        var resourceUrl =  'api/_search/company-managers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
