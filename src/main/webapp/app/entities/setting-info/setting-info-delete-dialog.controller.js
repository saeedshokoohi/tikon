(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('SettingInfoDeleteController',SettingInfoDeleteController);

    SettingInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'SettingInfo'];

    function SettingInfoDeleteController($uibModalInstance, entity, SettingInfo) {
        var vm = this;

        vm.settingInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SettingInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
