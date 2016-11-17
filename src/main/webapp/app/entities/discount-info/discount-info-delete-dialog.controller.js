(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('DiscountInfoDeleteController',DiscountInfoDeleteController);

    DiscountInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'DiscountInfo'];

    function DiscountInfoDeleteController($uibModalInstance, entity, DiscountInfo) {
        var vm = this;

        vm.discountInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DiscountInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
