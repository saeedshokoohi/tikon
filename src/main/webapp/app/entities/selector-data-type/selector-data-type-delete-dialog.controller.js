(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('SelectorDataTypeDeleteController',SelectorDataTypeDeleteController);

    SelectorDataTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'SelectorDataType'];

    function SelectorDataTypeDeleteController($uibModalInstance, entity, SelectorDataType) {
        var vm = this;

        vm.selectorDataType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SelectorDataType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
