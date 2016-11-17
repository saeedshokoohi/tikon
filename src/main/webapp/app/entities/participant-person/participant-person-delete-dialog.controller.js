(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ParticipantPersonDeleteController',ParticipantPersonDeleteController);

    ParticipantPersonDeleteController.$inject = ['$uibModalInstance', 'entity', 'ParticipantPerson'];

    function ParticipantPersonDeleteController($uibModalInstance, entity, ParticipantPerson) {
        var vm = this;

        vm.participantPerson = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ParticipantPerson.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
