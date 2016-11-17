(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('InvoiceInfoDetailController', InvoiceInfoDetailController);

    InvoiceInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InvoiceInfo', 'OrderBag', 'PaymentLog', 'Customer'];

    function InvoiceInfoDetailController($scope, $rootScope, $stateParams, entity, InvoiceInfo, OrderBag, PaymentLog, Customer) {
        var vm = this;

        vm.invoiceInfo = entity;

        var unsubscribe = $rootScope.$on('tikonApp:invoiceInfoUpdate', function(event, result) {
            vm.invoiceInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
