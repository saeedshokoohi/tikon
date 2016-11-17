(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceOptionInfoDialogController', ServiceOptionInfoDialogController);

    ServiceOptionInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ServiceOptionInfo', 'ServiceItem'];

    function ServiceOptionInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ServiceOptionInfo, ServiceItem) {
        var vm = this;

        vm.serviceOptionInfo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.serviceitems = ServiceItem.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.serviceOptionInfo.id !== null) {
                ServiceOptionInfo.update(vm.serviceOptionInfo, onSaveSuccess, onSaveError);
            } else {
                ServiceOptionInfo.save(vm.serviceOptionInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:serviceOptionInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
