(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('WaitingListDeleteController',WaitingListDeleteController);

    WaitingListDeleteController.$inject = ['$uibModalInstance', 'entity', 'WaitingList'];

    function WaitingListDeleteController($uibModalInstance, entity, WaitingList) {
        var vm = this;

        vm.waitingList = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WaitingList.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
