(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('OrderBagServiceItemDeleteController',OrderBagServiceItemDeleteController);

    OrderBagServiceItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'OrderBagServiceItem'];

    function OrderBagServiceItemDeleteController($uibModalInstance, entity, OrderBagServiceItem) {
        var vm = this;

        vm.orderBagServiceItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OrderBagServiceItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
