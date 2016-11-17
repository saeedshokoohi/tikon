(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('OrderBagServiceItemDetailController', OrderBagServiceItemDetailController);

    OrderBagServiceItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'OrderBagServiceItem', 'OrderBag', 'ServiceItem'];

    function OrderBagServiceItemDetailController($scope, $rootScope, $stateParams, entity, OrderBagServiceItem, OrderBag, ServiceItem) {
        var vm = this;

        vm.orderBagServiceItem = entity;

        var unsubscribe = $rootScope.$on('tikonApp:orderBagServiceItemUpdate', function(event, result) {
            vm.orderBagServiceItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
