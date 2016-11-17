(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('ServiceTimeSessionInfo', ServiceTimeSessionInfo);

    ServiceTimeSessionInfo.$inject = ['$resource'];

    function ServiceTimeSessionInfo ($resource) {
        var resourceUrl =  'api/service-time-session-infos/:id';

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
