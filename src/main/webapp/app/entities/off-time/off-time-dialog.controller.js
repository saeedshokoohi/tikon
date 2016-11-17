(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('OffTimeDialogController', OffTimeDialogController);

    OffTimeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OffTime', 'TimePeriod'];

    function OffTimeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OffTime, TimePeriod) {
        var vm = this;

        vm.offTime = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.timeperiods = TimePeriod.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.offTime.id !== null) {
                OffTime.update(vm.offTime, onSaveSuccess, onSaveError);
            } else {
                OffTime.save(vm.offTime, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:offTimeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fromTime = false;
        vm.datePickerOpenStatus.toTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
