(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('FinancialSettingDeleteController',FinancialSettingDeleteController);

    FinancialSettingDeleteController.$inject = ['$uibModalInstance', 'entity', 'FinancialSetting'];

    function FinancialSettingDeleteController($uibModalInstance, entity, FinancialSetting) {
        var vm = this;

        vm.financialSetting = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FinancialSetting.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
