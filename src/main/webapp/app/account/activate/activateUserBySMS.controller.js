(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ActivationBySMSController', ActivationBySMSController);

    ActivationBySMSController.$inject = ['$stateParams', 'Auth','entity' ,'LoginService'];

    function ActivationBySMSController ($stateParams, Auth,entity, LoginService) {

        debugger;
        var vm = this;
        vm.User = entity;
        vm.phoneNumber = null;
        vm.activateUser = activateUser;


        //Auth.activateAccount({key: $stateParams.key}).then(function () {
        //    vm.error = null;
        //    vm.success = 'OK'
        //    vm.error = 'ERROR';;
        //}).catch(function () {
        //    vm.success = null;
        //});


        function  activateUser ()
        {
            debugger;
            if(vm.phoneNumber !=null)
            {
                vm.key = vm.phoneNumber;
                Auth.activateAccount({key: vm.key}).then(function () {
                    vm.error = null;
                    vm.success = 'OK';
                }).catch(function () {
                    debugger;
                    vm.success = null;
                    vm.error = 'ERROR';
                });
                debugger;
                vm.login = LoginService.open;
            }
            else
            {
                vm.success = null;
                vm.error = 'ERROR';
            }




        }


    }
})();
