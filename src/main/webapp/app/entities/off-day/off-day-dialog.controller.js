(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('OffDayDialogController', OffDayDialogController);

    OffDayDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OffDay', 'DatePeriod'];

    function OffDayDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OffDay, DatePeriod) {
        var vm = this;

        vm.offDay = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.dateperiods = DatePeriod.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.offDay.id !== null) {
                OffDay.update(vm.offDay, onSaveSuccess, onSaveError);
            } else {
                OffDay.save(vm.offDay, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:offDayUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.offDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
