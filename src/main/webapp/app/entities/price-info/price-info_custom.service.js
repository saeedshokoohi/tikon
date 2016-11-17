(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('PriceInfoCustom', PriceInfoCustom);

    PriceInfoCustom.$inject = ['$resource'];

    function PriceInfoCustom ($resource) {



        var PriceInfosByServiceItem=function(serviceItemId)
        {
            var resourceUrl='api/price-infos-by-service-item/'+serviceItemId;
            return $resource(resourceUrl, {}, {
                    'query': { method: 'GET', isArray: true}
                }

            );
        };



        return {
            PriceInfosByServiceItem:PriceInfosByServiceItem
        };
    }
})();
