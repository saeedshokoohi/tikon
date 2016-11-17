(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('WaitingList', WaitingList);

    WaitingList.$inject = ['$resource', 'DateUtils'];

    function WaitingList ($resource, DateUtils) {
        var resourceUrl =  'api/waiting-lists/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.reserveTime = DateUtils.convertDateTimeFromServer(data.reserveTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
