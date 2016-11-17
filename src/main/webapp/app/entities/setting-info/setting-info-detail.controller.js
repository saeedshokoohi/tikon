(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('SettingInfoDetailController', SettingInfoDetailController);

    SettingInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'SettingInfo', 'ThemeSettingInfo', 'NotificationSetting', 'FinancialSetting'];

    function SettingInfoDetailController($scope, $rootScope, $stateParams, entity, SettingInfo, ThemeSettingInfo, NotificationSetting, FinancialSetting) {
        var vm = this;

        vm.settingInfo = entity;

        var unsubscribe = $rootScope.$on('tikonApp:settingInfoUpdate', function(event, result) {
            vm.settingInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
