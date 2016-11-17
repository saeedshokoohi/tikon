(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('OffDayDeleteController',OffDayDeleteController);

    OffDayDeleteController.$inject = ['$uibModalInstance', 'entity', 'OffDay'];

    function OffDayDeleteController($uibModalInstance, entity, OffDay) {
        var vm = this;

        vm.offDay = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OffDay.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
