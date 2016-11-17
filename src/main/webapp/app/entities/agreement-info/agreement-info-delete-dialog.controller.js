(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('AgreementInfoDeleteController',AgreementInfoDeleteController);

    AgreementInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'AgreementInfo'];

    function AgreementInfoDeleteController($uibModalInstance, entity, AgreementInfo) {
        var vm = this;

        vm.agreementInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AgreementInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
