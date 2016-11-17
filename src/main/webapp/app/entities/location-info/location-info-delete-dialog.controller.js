(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('LocationInfoDeleteController',LocationInfoDeleteController);

    LocationInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'LocationInfo'];

    function LocationInfoDeleteController($uibModalInstance, entity, LocationInfo) {
        var vm = this;

        vm.locationInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LocationInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
