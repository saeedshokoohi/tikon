(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('PriceInfoDtailCustom', PriceInfoDtailCustom);

    PriceInfoDtailCustom.$inject = ['$resource'];

    function PriceInfoDtailCustom ($resource) {



        var PriceInfoDtailsByServiceItem=function(serviceItemId)
        {
            var resourceUrl='api/price-info-dtails-by-service-item/'+serviceItemId;
            return $resource(resourceUrl, {}, {
                    'query': { method: 'GET', isArray: true}
                }

            );
        };



        return {
            PriceInfoDtailsByServiceItem:PriceInfoDtailsByServiceItem
        };
    }
})();
