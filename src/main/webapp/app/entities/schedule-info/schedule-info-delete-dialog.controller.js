(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ScheduleInfoDeleteController',ScheduleInfoDeleteController);

    ScheduleInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'ScheduleInfo'];

    function ScheduleInfoDeleteController($uibModalInstance, entity, ScheduleInfo) {
        var vm = this;

        vm.scheduleInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ScheduleInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
