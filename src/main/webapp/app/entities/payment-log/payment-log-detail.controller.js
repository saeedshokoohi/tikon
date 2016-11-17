(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('PaymentLogDetailController', PaymentLogDetailController);

    PaymentLogDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PaymentLog', 'InvoiceInfo'];

    function PaymentLogDetailController($scope, $rootScope, $stateParams, entity, PaymentLog, InvoiceInfo) {
        var vm = this;

        vm.paymentLog = entity;

        var unsubscribe = $rootScope.$on('tikonApp:paymentLogUpdate', function(event, result) {
            vm.paymentLog = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
