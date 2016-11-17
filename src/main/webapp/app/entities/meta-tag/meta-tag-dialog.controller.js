(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('MetaTagDialogController', MetaTagDialogController);

    MetaTagDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MetaTag', 'Company', 'ServiceItem'];

    function MetaTagDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MetaTag, Company, ServiceItem) {
        var vm = this;

        vm.metaTag = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();
        vm.serviceitems = ServiceItem.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.metaTag.id !== null) {
                MetaTag.update(vm.metaTag, onSaveSuccess, onSaveError);
            } else {
                MetaTag.save(vm.metaTag, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:metaTagUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
