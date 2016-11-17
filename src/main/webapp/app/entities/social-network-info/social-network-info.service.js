(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('SocialNetworkInfo', SocialNetworkInfo);

    SocialNetworkInfo.$inject = ['$resource'];

    function SocialNetworkInfo ($resource) {
        var resourceUrl =  'api/social-network-infos/:id';

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
