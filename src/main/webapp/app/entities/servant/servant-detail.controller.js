(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServantDetailController', ServantDetailController);

    ServantDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Servant', 'PersonInfo', 'ServiceCategory', 'ServiceItem', 'PriceInfo'];

    function ServantDetailController($scope, $rootScope, $stateParams, entity, Servant, PersonInfo, ServiceCategory, ServiceItem, PriceInfo) {
        var vm = this;

        vm.servant = entity;

        var unsubscribe = $rootScope.$on('tikonApp:servantUpdate', function(event, result) {
            vm.servant = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
