(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ExtraSettingDialogController', ExtraSettingDialogController);

    ExtraSettingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ExtraSetting', 'SettingInfo'];

    function ExtraSettingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ExtraSetting, SettingInfo) {
        var vm = this;

        vm.extraSetting = entity;
        vm.clear = clear;
        vm.save = save;
        vm.settinginfos = SettingInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.extraSetting.id !== null) {
                ExtraSetting.update(vm.extraSetting, onSaveSuccess, onSaveError);
            } else {
                ExtraSetting.save(vm.extraSetting, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:extraSettingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
