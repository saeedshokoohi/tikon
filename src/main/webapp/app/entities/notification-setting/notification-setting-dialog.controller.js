(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('NotificationSettingDialogController', NotificationSettingDialogController);

    NotificationSettingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'NotificationSetting'];

    function NotificationSettingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, NotificationSetting) {
        var vm = this;

        vm.notificationSetting = entity;
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
            if (vm.notificationSetting.id !== null) {
                NotificationSetting.update(vm.notificationSetting, onSaveSuccess, onSaveError);
            } else {
                NotificationSetting.save(vm.notificationSetting, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:notificationSettingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
