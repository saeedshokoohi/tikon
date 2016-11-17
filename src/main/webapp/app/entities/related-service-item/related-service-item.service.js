(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('RelatedServiceItem', RelatedServiceItem);

    RelatedServiceItem.$inject = ['$resource'];

    function RelatedServiceItem ($resource) {
        var resourceUrl =  'api/related-service-items/:id';

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
