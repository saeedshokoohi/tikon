(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('SelectorData', SelectorData);

    SelectorData.$inject = ['$resource'];

    function SelectorData ($resource) {
        var resourceUrl =  'api/selector-data/:id';

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
        }

        );
    }
})();
