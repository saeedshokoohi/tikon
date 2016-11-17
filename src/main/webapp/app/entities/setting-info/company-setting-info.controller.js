(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CompanySettingInfoController', CompanySettingInfoController);

    CompanySettingInfoController.$inject = ['$scope', '$state', 'SettingInfo', 'SettingInfoCustom', 'ParseLinks', 'AlertService' ];

    function CompanySettingInfoController ($scope, $state, SettingInfo, SettingInfoCustom, ParseLinks, AlertService ) {
        var vm = this;
        debugger;
        vm.loadPage = loadPage;
        vm.save = save;
        vm.settingInfo = {};
        loadPage();

        //function loadAll () {
        //
        //    function onSuccess(data, headers) {
        //        vm.settingInfos = data;
        //    }
        //    function onError(error) {
        //        AlertService.error(error.data.message);
        //    }
        //}

        function save () {
            debugger;
            vm.isSaving = true;
            if (vm.settingInfo.id !== null) {
                SettingInfo.update(vm.settingInfo, onSaveSuccess, onSaveError);
            } else {
                SettingInfoCustom.saveWithCompany().save(vm.settingInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:settingInfoUpdate', result);
            //$uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        function loadPage () {
            debugger;

            vm.settingInfo = SettingInfoCustom.getSettingInfoByCurrentCompany().query();
        }


    }
})();
