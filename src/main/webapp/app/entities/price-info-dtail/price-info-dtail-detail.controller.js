(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('PriceInfoDtailDetailController', PriceInfoDtailDetailController);

    PriceInfoDtailDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PriceInfoDtail'];

    function PriceInfoDtailDetailController($scope, $rootScope, $stateParams, entity, PriceInfoDtail) {
        var vm = this;

        vm.priceInfoDtail = entity;

        var unsubscribe = $rootScope.$on('tikonApp:priceInfoDtailUpdate', function(event, result) {
            vm.priceInfoDtail = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
