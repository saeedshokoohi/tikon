(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceTimeSessionDialogController', ServiceTimeSessionDialogController);

    ServiceTimeSessionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ServiceTimeSession', 'ServiceTimeSessionInfo'];

    function ServiceTimeSessionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ServiceTimeSession, ServiceTimeSessionInfo) {
        var vm = this;

        vm.serviceTimeSession = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.servicetimesessioninfos = ServiceTimeSessionInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.serviceTimeSession.id !== null) {
                ServiceTimeSession.update(vm.serviceTimeSession, onSaveSuccess, onSaveError);
            } else {
                ServiceTimeSession.save(vm.serviceTimeSession, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:serviceTimeSessionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startTime = false;
        vm.datePickerOpenStatus.endTime = false;
        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
