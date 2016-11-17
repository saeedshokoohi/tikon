(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('WeeklyWorkDayDeleteController',WeeklyWorkDayDeleteController);

    WeeklyWorkDayDeleteController.$inject = ['$uibModalInstance', 'entity', 'WeeklyWorkDay'];

    function WeeklyWorkDayDeleteController($uibModalInstance, entity, WeeklyWorkDay) {
        var vm = this;

        vm.weeklyWorkDay = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WeeklyWorkDay.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
