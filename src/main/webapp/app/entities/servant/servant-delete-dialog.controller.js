(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServantDeleteController',ServantDeleteController);

    ServantDeleteController.$inject = ['$uibModalInstance', 'entity', 'Servant'];

    function ServantDeleteController($uibModalInstance, entity, Servant) {
        var vm = this;

        vm.servant = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Servant.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
