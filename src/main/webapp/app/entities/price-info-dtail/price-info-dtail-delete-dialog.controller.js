(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('PriceInfoDtailDeleteController',PriceInfoDtailDeleteController);

    PriceInfoDtailDeleteController.$inject = ['$uibModalInstance', 'entity', 'PriceInfoDtail'];

    function PriceInfoDtailDeleteController($uibModalInstance, entity, PriceInfoDtail) {
        var vm = this;

        vm.priceInfoDtail = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PriceInfoDtail.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
