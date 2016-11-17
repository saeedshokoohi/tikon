(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('SocialNetworkInfoSearch', SocialNetworkInfoSearch);

    SocialNetworkInfoSearch.$inject = ['$resource'];

    function SocialNetworkInfoSearch($resource) {
        var resourceUrl =  'api/_search/social-network-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
