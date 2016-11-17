(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('PersonInfoDeleteController',PersonInfoDeleteController);

    PersonInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'PersonInfo'];

    function PersonInfoDeleteController($uibModalInstance, entity, PersonInfo) {
        var vm = this;

        vm.personInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PersonInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
