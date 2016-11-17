(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('SelectorDataDeleteController',SelectorDataDeleteController);

    SelectorDataDeleteController.$inject = ['$uibModalInstance', 'entity', 'SelectorData'];

    function SelectorDataDeleteController($uibModalInstance, entity, SelectorData) {
        var vm = this;

        vm.selectorData = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SelectorData.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
