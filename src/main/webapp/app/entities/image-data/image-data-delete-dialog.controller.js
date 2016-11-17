(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ImageDataDeleteController',ImageDataDeleteController);

    ImageDataDeleteController.$inject = ['$uibModalInstance', 'entity', 'ImageData'];

    function ImageDataDeleteController($uibModalInstance, entity, ImageData) {
        var vm = this;

        vm.imageData = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ImageData.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
