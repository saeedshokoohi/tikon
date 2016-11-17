(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ScheduleBaseDiscountDeleteController',ScheduleBaseDiscountDeleteController);

    ScheduleBaseDiscountDeleteController.$inject = ['$uibModalInstance', 'entity', 'ScheduleBaseDiscount'];

    function ScheduleBaseDiscountDeleteController($uibModalInstance, entity, ScheduleBaseDiscount) {
        var vm = this;

        vm.scheduleBaseDiscount = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ScheduleBaseDiscount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
