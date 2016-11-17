(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('DatePeriodDeleteController',DatePeriodDeleteController);

    DatePeriodDeleteController.$inject = ['$uibModalInstance', 'entity', 'DatePeriod'];

    function DatePeriodDeleteController($uibModalInstance, entity, DatePeriod) {
        var vm = this;

        vm.datePeriod = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DatePeriod.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
