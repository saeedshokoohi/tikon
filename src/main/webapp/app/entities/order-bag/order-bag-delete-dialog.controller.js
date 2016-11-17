(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('OrderBagDeleteController',OrderBagDeleteController);

    OrderBagDeleteController.$inject = ['$uibModalInstance', 'entity', 'OrderBag'];

    function OrderBagDeleteController($uibModalInstance, entity, OrderBag) {
        var vm = this;

        vm.orderBag = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OrderBag.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
