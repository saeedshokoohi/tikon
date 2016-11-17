(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('RelatedServiceItemDeleteController',RelatedServiceItemDeleteController);

    RelatedServiceItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'RelatedServiceItem'];

    function RelatedServiceItemDeleteController($uibModalInstance, entity, RelatedServiceItem) {
        var vm = this;

        vm.relatedServiceItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RelatedServiceItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
