(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceCapacityInfoDialogController', ServiceCapacityInfoDialogController);

    ServiceCapacityInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ServiceCapacityInfo'];

    function ServiceCapacityInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ServiceCapacityInfo) {
        var vm = this;

        vm.serviceCapacityInfo = entity;
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
            if (vm.serviceCapacityInfo.id !== null) {
                ServiceCapacityInfo.update(vm.serviceCapacityInfo, onSaveSuccess, onSaveError);
            } else {
                ServiceCapacityInfo.save(vm.serviceCapacityInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:serviceCapacityInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
