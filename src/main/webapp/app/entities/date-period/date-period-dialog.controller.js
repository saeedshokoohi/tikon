(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('DatePeriodDialogController', DatePeriodDialogController);

    DatePeriodDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DatePeriod', 'OffDay', 'WeeklyScheduleInfo'];

    function DatePeriodDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DatePeriod, OffDay, WeeklyScheduleInfo) {
        var vm = this;

        vm.datePeriod = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.offdays = OffDay.query();
        vm.weeklyscheduleinfos = WeeklyScheduleInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.datePeriod.id !== null) {
                DatePeriod.update(vm.datePeriod, onSaveSuccess, onSaveError);
            } else {
                DatePeriod.save(vm.datePeriod, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:datePeriodUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fromDate = false;
        vm.datePickerOpenStatus.toDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
