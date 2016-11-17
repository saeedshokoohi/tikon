(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('FinancialSettingDialogController', FinancialSettingDialogController);

    FinancialSettingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FinancialSetting'];

    function FinancialSettingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FinancialSetting) {
        var vm = this;

        vm.financialSetting = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.financialSetting.id !== null) {
                FinancialSetting.update(vm.financialSetting, onSaveSuccess, onSaveError);
            } else {
                FinancialSetting.save(vm.financialSetting, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:financialSettingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
