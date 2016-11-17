(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('AlbumInfoDeleteController',AlbumInfoDeleteController);

    AlbumInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'AlbumInfo'];

    function AlbumInfoDeleteController($uibModalInstance, entity, AlbumInfo) {
        var vm = this;

        vm.albumInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AlbumInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
