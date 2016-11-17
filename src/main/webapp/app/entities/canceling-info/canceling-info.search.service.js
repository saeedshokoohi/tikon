(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('CancelingInfoSearch', CancelingInfoSearch);

    CancelingInfoSearch.$inject = ['$resource'];

    function CancelingInfoSearch($resource) {
        var resourceUrl =  'api/_search/canceling-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
