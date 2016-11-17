(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('MetaTag', MetaTag);

    MetaTag.$inject = ['$resource'];

    function MetaTag ($resource) {
        var resourceUrl =  'api/meta-tags/:id';

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
