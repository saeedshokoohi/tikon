(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('OrderBagServiceItemDtailDetailController', OrderBagServiceItemDtailDetailController);

    OrderBagServiceItemDtailDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'OrderBagServiceItemDtail', 'OrderBagServiceItem', 'PriceInfoDtail'];

    function OrderBagServiceItemDtailDetailController($scope, $rootScope, $stateParams, entity, OrderBagServiceItemDtail, OrderBagServiceItem, PriceInfoDtail) {
        var vm = this;

        vm.orderBagServiceItemDtail = entity;

        var unsubscribe = $rootScope.$on('tikonApp:orderBagServiceItemDtailUpdate', function(event, result) {
            vm.orderBagServiceItemDtail = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
