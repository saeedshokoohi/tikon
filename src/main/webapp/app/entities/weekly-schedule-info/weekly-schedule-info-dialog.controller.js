(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('WeeklyScheduleInfoDialogController', WeeklyScheduleInfoDialogController);

    WeeklyScheduleInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WeeklyScheduleInfo', 'DatePeriod', 'TimePeriod', 'WeeklyWorkDay'];

    function WeeklyScheduleInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WeeklyScheduleInfo, DatePeriod, TimePeriod, WeeklyWorkDay) {
        var vm = this;

        vm.weeklyScheduleInfo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.dateperiods = DatePeriod.query();
        vm.timeperiods = TimePeriod.query();
        vm.weeklyworkdays = WeeklyWorkDay.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.weeklyScheduleInfo.id !== null) {
                WeeklyScheduleInfo.update(vm.weeklyScheduleInfo, onSaveSuccess, onSaveError);
            } else {
                WeeklyScheduleInfo.save(vm.weeklyScheduleInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:weeklyScheduleInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
