(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ScheduleBaseDiscountDialogController', ScheduleBaseDiscountDialogController);

    ScheduleBaseDiscountDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ScheduleBaseDiscount', 'ScheduleInfo', 'DiscountInfo'];

    function ScheduleBaseDiscountDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ScheduleBaseDiscount, ScheduleInfo, DiscountInfo) {
        var vm = this;

        vm.scheduleBaseDiscount = entity;
        vm.clear = clear;
        vm.save = save;
        vm.scheduleinfos = ScheduleInfo.query();
        vm.discountinfos = DiscountInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.scheduleBaseDiscount.id !== null) {
                ScheduleBaseDiscount.update(vm.scheduleBaseDiscount, onSaveSuccess, onSaveError);
            } else {
                ScheduleBaseDiscount.save(vm.scheduleBaseDiscount, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:scheduleBaseDiscountUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
