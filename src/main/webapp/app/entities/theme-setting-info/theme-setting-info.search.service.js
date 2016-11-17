(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('ThemeSettingInfoSearch', ThemeSettingInfoSearch);

    ThemeSettingInfoSearch.$inject = ['$resource'];

    function ThemeSettingInfoSearch($resource) {
        var resourceUrl =  'api/_search/theme-setting-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
