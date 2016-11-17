(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('PriceInfoDeleteController',PriceInfoDeleteController);

    PriceInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'PriceInfo'];

    function PriceInfoDeleteController($uibModalInstance, entity, PriceInfo) {
        var vm = this;

        vm.priceInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PriceInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
