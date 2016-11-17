(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('ExtraSettingSearch', ExtraSettingSearch);

    ExtraSettingSearch.$inject = ['$resource'];

    function ExtraSettingSearch($resource) {
        var resourceUrl =  'api/_search/extra-settings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
