(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('AgreementInfoSearch', AgreementInfoSearch);

    AgreementInfoSearch.$inject = ['$resource'];

    function AgreementInfoSearch($resource) {
        var resourceUrl =  'api/_search/agreement-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
