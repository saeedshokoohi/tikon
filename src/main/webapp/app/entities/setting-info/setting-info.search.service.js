(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('SettingInfoSearch', SettingInfoSearch);

    SettingInfoSearch.$inject = ['$resource'];

    function SettingInfoSearch($resource) {
        var resourceUrl =  'api/_search/setting-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
