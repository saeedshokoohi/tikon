(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('OrderBagDetailController', OrderBagDetailController);

    OrderBagDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'OrderBag'];

    function OrderBagDetailController($scope, $rootScope, $stateParams, entity, OrderBag) {
        var vm = this;

        vm.orderBag = entity;

        var unsubscribe = $rootScope.$on('tikonApp:orderBagUpdate', function(event, result) {
            vm.orderBag = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
