(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('TimePeriodDialogController', TimePeriodDialogController);

    TimePeriodDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TimePeriod', 'OffTime', 'WeeklyScheduleInfo'];

    function TimePeriodDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TimePeriod, OffTime, WeeklyScheduleInfo) {
        var vm = this;

        vm.timePeriod = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.offtimes = OffTime.query();
        vm.weeklyscheduleinfos = WeeklyScheduleInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.timePeriod.id !== null) {
                TimePeriod.update(vm.timePeriod, onSaveSuccess, onSaveError);
            } else {
                TimePeriod.save(vm.timePeriod, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:timePeriodUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startTime = false;
        vm.datePickerOpenStatus.endTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
