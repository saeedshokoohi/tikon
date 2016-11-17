(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ThemeSettingInfoDetailController', ThemeSettingInfoDetailController);

    ThemeSettingInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ThemeSettingInfo', 'ImageData'];

    function ThemeSettingInfoDetailController($scope, $rootScope, $stateParams, entity, ThemeSettingInfo, ImageData) {
        var vm = this;

        vm.themeSettingInfo = entity;

        var unsubscribe = $rootScope.$on('tikonApp:themeSettingInfoUpdate', function(event, result) {
            vm.themeSettingInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
