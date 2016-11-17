(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('OrderBagServiceItemDtailDeleteController',OrderBagServiceItemDtailDeleteController);

    OrderBagServiceItemDtailDeleteController.$inject = ['$uibModalInstance', 'entity', 'OrderBagServiceItemDtail'];

    function OrderBagServiceItemDtailDeleteController($uibModalInstance, entity, OrderBagServiceItemDtail) {
        var vm = this;

        vm.orderBagServiceItemDtail = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OrderBagServiceItemDtail.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
