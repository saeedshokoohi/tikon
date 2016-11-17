(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ThemeSettingInfoDialogController', ThemeSettingInfoDialogController);

    ThemeSettingInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ThemeSettingInfo', 'ImageData'];

    function ThemeSettingInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ThemeSettingInfo, ImageData) {
        var vm = this;

        vm.themeSettingInfo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.imagedata = ImageData.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.themeSettingInfo.id !== null) {
                ThemeSettingInfo.update(vm.themeSettingInfo, onSaveSuccess, onSaveError);
            } else {
                ThemeSettingInfo.save(vm.themeSettingInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:themeSettingInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
