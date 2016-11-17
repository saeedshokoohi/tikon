(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('PersonInfoDialogControllerCopy', PersonInfoDialogControllerCopy);

    PersonInfoDialogControllerCopy.$inject = ['$timeout', '$scope', '$stateParams',  'entity', 'PersonInfo', 'LocationInfo', 'SocialNetworkInfo'];
    // PersonInfoDialogControllerCopy.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PersonInfo', 'LocationInfo', 'SocialNetworkInfo'];

    function PersonInfoDialogControllerCopy ($timeout, $scope, $stateParams,  entity, PersonInfo, LocationInfo, SocialNetworkInfo) {
        var vm = this;

        vm.personInfo = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.locationinfos = LocationInfo.query();
        vm.socialnetworkinfos = SocialNetworkInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            // $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.personInfo.id !== null) {
                PersonInfo.update(vm.personInfo, onSaveSuccess, onSaveError);
            } else {
                PersonInfo.save(vm.personInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:personInfoUpdate', result);
            // $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.birthDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
