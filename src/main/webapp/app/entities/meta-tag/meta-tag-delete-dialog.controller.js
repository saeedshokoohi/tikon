(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('MetaTagDeleteController',MetaTagDeleteController);

    MetaTagDeleteController.$inject = ['$uibModalInstance', 'entity', 'MetaTag'];

    function MetaTagDeleteController($uibModalInstance, entity, MetaTag) {
        var vm = this;

        vm.metaTag = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MetaTag.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
