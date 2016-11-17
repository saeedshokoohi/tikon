(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ExtraSettingDetailController', ExtraSettingDetailController);

    ExtraSettingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ExtraSetting', 'SettingInfo'];

    function ExtraSettingDetailController($scope, $rootScope, $stateParams, entity, ExtraSetting, SettingInfo) {
        var vm = this;

        vm.extraSetting = entity;

        var unsubscribe = $rootScope.$on('tikonApp:extraSettingUpdate', function(event, result) {
            vm.extraSetting = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
