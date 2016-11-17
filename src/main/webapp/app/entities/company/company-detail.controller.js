(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CompanyDetailController', CompanyDetailController);

    CompanyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Company', 'SettingInfo', 'AgreementInfo', 'LocationInfo', 'MetaTag'];

    function CompanyDetailController($scope, $rootScope, $stateParams, entity, Company, SettingInfo, AgreementInfo, LocationInfo, MetaTag) {
        var vm = this;

        vm.company = entity;

        var unsubscribe = $rootScope.$on('tikonApp:companyUpdate', function(event, result) {
            vm.company = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
