(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CompanyManagerDetailController', CompanyManagerDetailController);

    CompanyManagerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CompanyManager', 'Company', 'PersonInfo'];

    function CompanyManagerDetailController($scope, $rootScope, $stateParams, entity, CompanyManager, Company, PersonInfo) {
        var vm = this;

        vm.companyManager = entity;

        var unsubscribe = $rootScope.$on('tikonApp:companyManagerUpdate', function(event, result) {
            vm.companyManager = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
