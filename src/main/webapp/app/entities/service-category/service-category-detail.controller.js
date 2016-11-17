(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceCategoryDetailController', ServiceCategoryDetailController);

    ServiceCategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ServiceCategory', 'SettingInfo', 'Company', 'Servant', 'AlbumInfo'];

    function ServiceCategoryDetailController($scope, $rootScope, $stateParams, entity, ServiceCategory, SettingInfo, Company, Servant, AlbumInfo) {
        var vm = this;

        vm.serviceCategory = entity;

        var unsubscribe = $rootScope.$on('tikonApp:serviceCategoryUpdate', function(event, result) {
            vm.serviceCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
