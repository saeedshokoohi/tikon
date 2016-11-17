(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('RelatedServiceItemDetailController', RelatedServiceItemDetailController);

    RelatedServiceItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'RelatedServiceItem', 'ServiceItem'];

    function RelatedServiceItemDetailController($scope, $rootScope, $stateParams, entity, RelatedServiceItem, ServiceItem) {
        var vm = this;

        vm.relatedServiceItem = entity;

        var unsubscribe = $rootScope.$on('tikonApp:relatedServiceItemUpdate', function(event, result) {
            vm.relatedServiceItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
