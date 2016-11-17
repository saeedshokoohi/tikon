(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('Servant', Servant);

    Servant.$inject = ['$resource'];

    function Servant ($resource) {
        var resourceUrl =  'api/servants/:id';

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
