(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CustomerCommentDeleteController',CustomerCommentDeleteController);

    CustomerCommentDeleteController.$inject = ['$uibModalInstance', 'entity', 'CustomerComment'];

    function CustomerCommentDeleteController($uibModalInstance, entity, CustomerComment) {
        var vm = this;

        vm.customerComment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CustomerComment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
