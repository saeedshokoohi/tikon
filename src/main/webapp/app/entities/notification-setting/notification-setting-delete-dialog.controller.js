(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('NotificationSettingDeleteController',NotificationSettingDeleteController);

    NotificationSettingDeleteController.$inject = ['$uibModalInstance', 'entity', 'NotificationSetting'];

    function NotificationSettingDeleteController($uibModalInstance, entity, NotificationSetting) {
        var vm = this;

        vm.notificationSetting = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            NotificationSetting.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
