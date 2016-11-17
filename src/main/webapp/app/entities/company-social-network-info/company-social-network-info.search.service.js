(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('CompanySocialNetworkInfoSearch', CompanySocialNetworkInfoSearch);

    CompanySocialNetworkInfoSearch.$inject = ['$resource'];

    function CompanySocialNetworkInfoSearch($resource) {
        var resourceUrl =  'api/_search/company-social-network-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
