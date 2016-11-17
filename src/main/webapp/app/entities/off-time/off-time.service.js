(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('OffTime', OffTime);

    OffTime.$inject = ['$resource', 'DateUtils'];

    function OffTime ($resource, DateUtils) {
        var resourceUrl =  'api/off-times/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fromTime = DateUtils.convertDateTimeFromServer(data.fromTime);
                        data.toTime = DateUtils.convertDateTimeFromServer(data.toTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
