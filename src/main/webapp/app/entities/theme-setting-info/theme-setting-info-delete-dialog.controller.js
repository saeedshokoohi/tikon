(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ThemeSettingInfoDeleteController',ThemeSettingInfoDeleteController);

    ThemeSettingInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'ThemeSettingInfo'];

    function ThemeSettingInfoDeleteController($uibModalInstance, entity, ThemeSettingInfo) {
        var vm = this;

        vm.themeSettingInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ThemeSettingInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
