(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('ServiceItemCustom', ServiceItemCustom);

    ServiceItemCustom.$inject = ['$resource'];

    function ServiceItemCustom ($resource) {



        var getServiceItemsByCurrentCompany=function()
        {
            debugger;
            var resourceUrl='api/service-items-by-company';
            return $resource(resourceUrl, {}, {
                    'query': { method: 'GET', isArray: true}
                }

            );
        };



        return {
            getServiceItemsByCurrentCompany:getServiceItemsByCurrentCompany
        };
    }
})();
