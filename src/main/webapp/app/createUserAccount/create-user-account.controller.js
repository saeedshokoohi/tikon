(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CreateUserAccountController', CreateUserAccountController);


    CreateUserAccountController.$inject = ['$translate', '$timeout', 'Auth', 'LoginService','PersonInfo','$state'];

    function CreateUserAccountController ($translate, $timeout, Auth, LoginService,PersonInfo,$state) {
        var vm = this;

        vm.doNotMatch = null;
        vm.error = null;
        vm.errorUserExists = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.registerAccount = {};
        vm.success = null;
        vm.activatedSuccess = null;
        vm.activateUser = activateUser;
        vm.submited =submited;
        vm.formMode = 'REGISTER';
        vm.errorPhoneNumberExists = null;
        vm.regenerateActivationKey = regenerateActivationKey;
        vm.activatedSuccess = null;

        $timeout(function (){angular.element('#login').focus();});

        function submited()
        {
            // not register yet
            if(vm.formMode == 'REGISTER')
            {
               register();
            }
            if(vm.formMode == 'ACTIVEUSER')
            {
                activateUser();
            }

        }

        function register () {

            if (vm.registerAccount.password !== vm.confirmPassword) {
                vm.doNotMatch = 'ERROR';
            } else {
                vm.registerAccount.langKey = $translate.use();
                vm.doNotMatch = null;
                vm.error = null;
                vm.errorUserExists = null;
                vm.errorEmailExists = null;
                vm.errorPhoneNumberExists = null;


                Auth.createAccount(vm.registerAccount).then(function () {
                    debugger;
                    vm.success = 'OK';
                    vm.formMode = 'ACTIVEUSER';
                }).catch(function (response) {
                    vm.success = null;
                    if (response.status === 400 && response.data === 'login already in use') {
                        vm.errorUserExists = 'ERROR';
                    } else if (response.status === 400 && response.data === 'e-mail address already in use') {
                        vm.errorEmailExists = 'ERROR';
                    } else if (response.status === 400 && response.data === 'PhoneNumber already in use') {
                        vm.errorPhoneNumberExists = 'ERROR';
                    } else {
                        vm.error = 'ERROR';
                    }


                });
            }
        }

        function  activateUser ()
        {
            debugger;
            if(vm.key !=null)
            {
                Auth.activateAccount(vm.registerAccount.login,vm.key).then(function () {
                    debugger;
                    vm.activateError = null;
                    vm.activatedSuccess = 'OK';
                }).catch(function () {
                    debugger;
                    vm.activatedSuccess = null;
                    vm.activateError = 'ERROR';
                });
            }
            else
            {
                debugger;
                vm.activatedSuccess = null;
                vm.activateError = 'ERROR';
            }

        }

        function regenerateActivationKey()
        {
            debugger;
            Auth.regenerateActivationKey(vm.registerAccount.login)
                .then(function(){
                    debugger;
                vm.regenerateSuccess ='OK';
                vm.regenerateError =null;
            }).catch(function () {
                debugger;
                vm.regenerateSuccess = null;
                vm.regenerateError = 'ERROR';
            });
        }
    }
})();
