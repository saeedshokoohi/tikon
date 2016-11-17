(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ExtraSettingDeleteController',ExtraSettingDeleteController);

    ExtraSettingDeleteController.$inject = ['$uibModalInstance', 'entity', 'ExtraSetting'];

    function ExtraSettingDeleteController($uibModalInstance, entity, ExtraSetting) {
        var vm = this;

        vm.extraSetting = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ExtraSetting.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
