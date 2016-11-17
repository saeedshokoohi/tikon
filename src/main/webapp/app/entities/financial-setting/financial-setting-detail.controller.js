(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('FinancialSettingDetailController', FinancialSettingDetailController);

    FinancialSettingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'FinancialSetting'];

    function FinancialSettingDetailController($scope, $rootScope, $stateParams, entity, FinancialSetting) {
        var vm = this;

        vm.financialSetting = entity;

        var unsubscribe = $rootScope.$on('tikonApp:financialSettingUpdate', function(event, result) {
            vm.financialSetting = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
