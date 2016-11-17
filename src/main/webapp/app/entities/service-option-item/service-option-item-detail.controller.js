(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceOptionItemDetailController', ServiceOptionItemDetailController);

    ServiceOptionItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ServiceOptionItem', 'ServiceOptionInfo', 'PriceInfo', 'AlbumInfo'];

    function ServiceOptionItemDetailController($scope, $rootScope, $stateParams, entity, ServiceOptionItem, ServiceOptionInfo, PriceInfo, AlbumInfo) {
        var vm = this;

        vm.serviceOptionItem = entity;

        var unsubscribe = $rootScope.$on('tikonApp:serviceOptionItemUpdate', function(event, result) {
            vm.serviceOptionItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
