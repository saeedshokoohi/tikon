(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('TimePeriodDeleteController',TimePeriodDeleteController);

    TimePeriodDeleteController.$inject = ['$uibModalInstance', 'entity', 'TimePeriod'];

    function TimePeriodDeleteController($uibModalInstance, entity, TimePeriod) {
        var vm = this;

        vm.timePeriod = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TimePeriod.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
