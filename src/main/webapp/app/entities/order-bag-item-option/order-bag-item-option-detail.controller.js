(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('OrderBagItemOptionDetailController', OrderBagItemOptionDetailController);

    OrderBagItemOptionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'OrderBagItemOption', 'OrderBagServiceItemDtail', 'ServiceOptionItem'];

    function OrderBagItemOptionDetailController($scope, $rootScope, $stateParams, entity, OrderBagItemOption, OrderBagServiceItemDtail, ServiceOptionItem) {
        var vm = this;

        vm.orderBagItemOption = entity;

        var unsubscribe = $rootScope.$on('tikonApp:orderBagItemOptionUpdate', function(event, result) {
            vm.orderBagItemOption = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
