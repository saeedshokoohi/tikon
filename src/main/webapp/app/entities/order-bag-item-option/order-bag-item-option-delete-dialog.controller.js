(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('OrderBagItemOptionDeleteController',OrderBagItemOptionDeleteController);

    OrderBagItemOptionDeleteController.$inject = ['$uibModalInstance', 'entity', 'OrderBagItemOption'];

    function OrderBagItemOptionDeleteController($uibModalInstance, entity, OrderBagItemOption) {
        var vm = this;

        vm.orderBagItemOption = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OrderBagItemOption.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
