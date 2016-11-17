(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('WeeklyScheduleInfoDeleteController',WeeklyScheduleInfoDeleteController);

    WeeklyScheduleInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'WeeklyScheduleInfo'];

    function WeeklyScheduleInfoDeleteController($uibModalInstance, entity, WeeklyScheduleInfo) {
        var vm = this;

        vm.weeklyScheduleInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WeeklyScheduleInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
