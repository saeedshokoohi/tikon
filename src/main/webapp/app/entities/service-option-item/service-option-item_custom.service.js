(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('ServiceOptionItemCustom', ServiceOptionItemCustom);

    ServiceOptionItemCustom.$inject = ['$resource'];

    function ServiceOptionItemCustom ($resource) {



        var ServiceOptionItemsByServiceItem=function(serviceItemId)
        {
            var resourceUrl='api/service-option-items-by-service-item/'+serviceItemId;
            return $resource(resourceUrl, {}, {
                    'query': { method: 'GET', isArray: true}
                }

            );
        };



        return {
            ServiceOptionItemsByServiceItem:ServiceOptionItemsByServiceItem
        };
    }
})();
