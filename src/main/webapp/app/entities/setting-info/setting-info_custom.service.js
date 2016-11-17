(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('SettingInfoCustom', SettingInfoCustom);

    SettingInfoCustom.$inject = ['$resource'];

    function SettingInfoCustom ($resource) {



        var getSettingInfoByCurrentCompany=function()
        {
            var resourceUrl='api/setting-info-by-current-company';
            return $resource(resourceUrl, {}, {
                    'query': { method: 'GET', isArray: false}
                }

            );
        };

        var saveWithCompany=function()
        {
            debugger;
            var resourceUrl='api/setting-info-with-company';
            return $resource(resourceUrl, {}, {});
        };




        return {
            getSettingInfoByCurrentCompany:getSettingInfoByCurrentCompany,
            saveWithCompany : saveWithCompany
        };
    }
})();
