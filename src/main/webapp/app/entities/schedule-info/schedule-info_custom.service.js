(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('ScheduleInfoCustom', ScheduleInfoCustom);

    ScheduleInfoCustom.$inject = ['$resource'];

    function ScheduleInfoCustom ($resource) {



        var primaryScheduleInfosByServiceItem=function(serviceItemId)
        {
            var resourceUrl='api/primary-schedule-info-by-service-item/'+serviceItemId;
            return $resource(resourceUrl, {}, {
                    'query': { method: 'GET', isArray: true}
                }

            );
        };



        return {
            primaryScheduleInfosByServiceItem:primaryScheduleInfosByServiceItem
        };
    }
})();
