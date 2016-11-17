(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ParticipantPersonDialogController', ParticipantPersonDialogController);

    ParticipantPersonDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ParticipantPerson', 'PersonInfo', 'OrderBagServiceItemDtail'];

    function ParticipantPersonDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ParticipantPerson, PersonInfo, OrderBagServiceItemDtail) {
        var vm = this;

        vm.participantPerson = entity;
        vm.clear = clear;
        vm.save = save;
        vm.personinfos = PersonInfo.query();
        vm.orderbagserviceitemdtails = OrderBagServiceItemDtail.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.participantPerson.id !== null) {
                ParticipantPerson.update(vm.participantPerson, onSaveSuccess, onSaveError);
            } else {
                ParticipantPerson.save(vm.participantPerson, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:participantPersonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
