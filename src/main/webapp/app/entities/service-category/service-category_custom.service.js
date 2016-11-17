(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('ServiceCategoryCustom', ServiceCategoryCustom);

    ServiceCategoryCustom.$inject = ['$resource'];

    function ServiceCategoryCustom ($resource) {



        var getServiceCategoriesByCurrentCompany=function()
        {
            debugger;
            var resourceUrl='api/service-categories-by-current-company';
            return $resource(resourceUrl, {}, {
                    'query': { method: 'GET', isArray: true}
                }

            );
        };



        return {
            getServiceCategoriesByCurrentCompany:getServiceCategoriesByCurrentCompany
        };
    }
})();
