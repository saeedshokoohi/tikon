(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CompanyServiceCategoryDialogController', CompanyServiceCategoryDialogController);

    CompanyServiceCategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ServiceCategory', 'SettingInfo', 'Company', 'Servant', 'AlbumInfo'];

    function CompanyServiceCategoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ServiceCategory, SettingInfo, Company, Servant, AlbumInfo) {
        var vm = this;

        vm.serviceCategory = entity;
        vm.clear = clear;
        vm.save = save;
        vm.settinginfos = SettingInfo.query();
        vm.companies = Company.query();
        vm.servicecategories = ServiceCategory.query();
        vm.servants = Servant.query();
        //vm.albuminfos = AlbumInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.serviceCategory.id !== null) {
                ServiceCategory.update(vm.serviceCategory, onSaveSuccess, onSaveError);
            } else {
                ServiceCategory.save(vm.serviceCategory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:serviceCategoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
