(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('RelatedServiceItemDialogController', RelatedServiceItemDialogController);

    RelatedServiceItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RelatedServiceItem', 'ServiceItem'];

    function RelatedServiceItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RelatedServiceItem, ServiceItem) {
        var vm = this;

        vm.relatedServiceItem = entity;
        vm.clear = clear;
        vm.save = save;
        vm.serviceitems = ServiceItem.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.relatedServiceItem.id !== null) {
                RelatedServiceItem.update(vm.relatedServiceItem, onSaveSuccess, onSaveError);
            } else {
                RelatedServiceItem.save(vm.relatedServiceItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:relatedServiceItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
