(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('ScheduleBaseDiscount', ScheduleBaseDiscount);

    ScheduleBaseDiscount.$inject = ['$resource'];

    function ScheduleBaseDiscount ($resource) {
        var resourceUrl =  'api/schedule-base-discounts/:id';

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
