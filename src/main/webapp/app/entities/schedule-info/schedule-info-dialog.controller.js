(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ScheduleInfoDialogController', ScheduleInfoDialogController);

    ScheduleInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ScheduleInfo', 'WeeklyScheduleInfo', 'ServiceItem'];

    function ScheduleInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ScheduleInfo, WeeklyScheduleInfo, ServiceItem) {
        var vm = this;

        vm.scheduleInfo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.weeklyscheduleinfos = WeeklyScheduleInfo.query();
        vm.serviceitems = ServiceItem.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.scheduleInfo.id !== null) {
                ScheduleInfo.update(vm.scheduleInfo, onSaveSuccess, onSaveError);
            } else {
                ScheduleInfo.save(vm.scheduleInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:scheduleInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
