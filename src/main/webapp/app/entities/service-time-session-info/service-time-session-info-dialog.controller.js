(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceTimeSessionInfoDialogController', ServiceTimeSessionInfoDialogController);

    ServiceTimeSessionInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ServiceTimeSessionInfo', 'ServiceItem', 'ScheduleInfo', 'ServiceTimeSession'];

    function ServiceTimeSessionInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ServiceTimeSessionInfo, ServiceItem, ScheduleInfo, ServiceTimeSession) {
        var vm = this;

        vm.serviceTimeSessionInfo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.serviceitems = ServiceItem.query();
        vm.scheduleinfos = ScheduleInfo.query();
        vm.servicetimesessions = ServiceTimeSession.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.serviceTimeSessionInfo.id !== null) {
                ServiceTimeSessionInfo.update(vm.serviceTimeSessionInfo, onSaveSuccess, onSaveError);
            } else {
                ServiceTimeSessionInfo.save(vm.serviceTimeSessionInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:serviceTimeSessionInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
