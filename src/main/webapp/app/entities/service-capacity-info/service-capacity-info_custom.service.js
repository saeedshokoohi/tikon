(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('ServiceCapacityInfoCustom', ServiceCapacityInfoCustom);

    ServiceCapacityInfoCustom.$inject = ['$resource'];

    function ServiceCapacityInfoCustom ($resource) {



        var ServiceCapacityInfoByServiceItem=function(serviceItemId)
        {
            var resourceUrl='api/service-capacity-info-by-service-item/'+serviceItemId;
            return $resource(resourceUrl, {}, {
                    'query': { method: 'GET', isArray: true}
                }

            );
        };



        return {
            ServiceCapacityInfoByServiceItem:ServiceCapacityInfoByServiceItem
        };
    }
})();
