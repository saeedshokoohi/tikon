(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('NotificationSettingSearch', NotificationSettingSearch);

    NotificationSettingSearch.$inject = ['$resource'];

    function NotificationSettingSearch($resource) {
        var resourceUrl =  'api/_search/notification-settings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
