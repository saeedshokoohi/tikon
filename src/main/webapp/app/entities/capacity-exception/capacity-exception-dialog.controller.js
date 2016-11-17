(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CapacityExceptionDialogController', CapacityExceptionDialogController);

    CapacityExceptionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CapacityException', 'ServiceCapacityInfo'];

    function CapacityExceptionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CapacityException, ServiceCapacityInfo) {
        var vm = this;

        vm.capacityException = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.servicecapacityinfos = ServiceCapacityInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.capacityException.id !== null) {
                CapacityException.update(vm.capacityException, onSaveSuccess, onSaveError);
            } else {
                CapacityException.save(vm.capacityException, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:capacityExceptionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.reserveTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
