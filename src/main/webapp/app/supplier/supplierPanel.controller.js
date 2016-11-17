(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('SupplierPanelController', SetupController);

    SetupController.$inject = ['$scope', 'Principal', 'LoginService', '$state'];

    function SetupController ($scope, Principal, LoginService, $state) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
    }
})();
