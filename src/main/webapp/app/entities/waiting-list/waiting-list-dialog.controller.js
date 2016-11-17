(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('WaitingListDialogController', WaitingListDialogController);

    WaitingListDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WaitingList', 'ServiceItem'];

    function WaitingListDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WaitingList, ServiceItem) {
        var vm = this;

        vm.waitingList = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            if (vm.waitingList.id !== null) {
                WaitingList.update(vm.waitingList, onSaveSuccess, onSaveError);
            } else {
                WaitingList.save(vm.waitingList, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:waitingListUpdate', result);
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
