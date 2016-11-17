(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('ScheduleBaseDiscountSearch', ScheduleBaseDiscountSearch);

    ScheduleBaseDiscountSearch.$inject = ['$resource'];

    function ScheduleBaseDiscountSearch($resource) {
        var resourceUrl =  'api/_search/schedule-base-discounts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
