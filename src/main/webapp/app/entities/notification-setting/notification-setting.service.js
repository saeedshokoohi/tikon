(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('NotificationSetting', NotificationSetting);

    NotificationSetting.$inject = ['$resource'];

    function NotificationSetting ($resource) {
        var resourceUrl =  'api/notification-settings/:id';

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
