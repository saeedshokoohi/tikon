(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('PriceInfoDetailController', PriceInfoDetailController);

    PriceInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PriceInfo', 'Servant'];

    function PriceInfoDetailController($scope, $rootScope, $stateParams, entity, PriceInfo, Servant) {
        var vm = this;

        vm.priceInfo = entity;

        var unsubscribe = $rootScope.$on('tikonApp:priceInfoUpdate', function(event, result) {
            vm.priceInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
