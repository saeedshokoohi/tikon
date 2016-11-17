(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('ScheduleInfoSearch', ScheduleInfoSearch);

    ScheduleInfoSearch.$inject = ['$resource'];

    function ScheduleInfoSearch($resource) {
        var resourceUrl =  'api/_search/schedule-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
