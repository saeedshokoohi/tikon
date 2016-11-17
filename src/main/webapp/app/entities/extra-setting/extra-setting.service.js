(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('ExtraSetting', ExtraSetting);

    ExtraSetting.$inject = ['$resource'];

    function ExtraSetting ($resource) {
        var resourceUrl =  'api/extra-settings/:id';

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
