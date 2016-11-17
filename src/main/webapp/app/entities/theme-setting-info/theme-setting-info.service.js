(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('ThemeSettingInfo', ThemeSettingInfo);

    ThemeSettingInfo.$inject = ['$resource'];

    function ThemeSettingInfo ($resource) {
        var resourceUrl =  'api/theme-setting-infos/:id';

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
