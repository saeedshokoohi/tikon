(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('NotificationSettingDetailController', NotificationSettingDetailController);

    NotificationSettingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'NotificationSetting'];

    function NotificationSettingDetailController($scope, $rootScope, $stateParams, entity, NotificationSetting) {
        var vm = this;

        vm.notificationSetting = entity;

        var unsubscribe = $rootScope.$on('tikonApp:notificationSettingUpdate', function(event, result) {
            vm.notificationSetting = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
