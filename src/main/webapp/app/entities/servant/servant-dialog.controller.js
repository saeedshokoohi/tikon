(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServantDialogController', ServantDialogController);

    ServantDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Servant', 'PersonInfo', 'ServiceCategory', 'ServiceItem', 'PriceInfo'];

    function ServantDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Servant, PersonInfo, ServiceCategory, ServiceItem, PriceInfo) {
        var vm = this;

        vm.servant = entity;
        vm.clear = clear;
        vm.save = save;
        vm.personinfos = PersonInfo.query();
        vm.servicecategories = ServiceCategory.query();
        vm.serviceitems = ServiceItem.query();
        vm.priceinfos = PriceInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.servant.id !== null) {
                Servant.update(vm.servant, onSaveSuccess, onSaveError);
            } else {
                Servant.save(vm.servant, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:servantUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
