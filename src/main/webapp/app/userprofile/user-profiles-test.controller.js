// (function() {
//     'use strict';
//
//     angular
//         .module('tikonApp')
//         .controller('UserProfileController', UserProfileController);
//
//     UserProfileController.$inject = ['Principal', 'Auth' , 'PersonInfo' ,'PersonInfoQuery' , '$translate','$scope'];
//
//     function UserProfileController (Principal, Auth,PersonInfo,PersonInfoQuery,  $translate,$scope) {
//         var vm = this;
//         vm.accid   = null;
//         vm.error = null;
//         vm.save = save;
//         vm.loadPersonClick= loadPersonClick;
//         vm.settingsAccount = null;
//         vm.success = null;
//         vm.myName=['saeed','majid'];
//         vm.personInfoDTO = '';
//         vm.personInfoDTOlist = ['test','mest'];
//         vm.userName = 'majiduser';
//         //vm.firstName = 'majid';
//         //vm.lastName = 'fattahi';
//         vm.email= 'magid_plus_@yahoo.com'
//         vm.success = true;
//         vm.nationalCode = null;
//         vm.personID = null;
//
//         /**
//          * Store the "settings account" in a separate variable, and not in the shared "account" variable.
//          */
//         var copyAccount = function (account) {
//             return {
//
//                 activated: account.activated,
//                 email: account.email,
//                 firstName: 'majid',//account.firstName,
//                 langKey: account.langKey,
//                 lastName: 'fattahi',//account.lastName,
//                 login: account.login,
//                 id:account.id
//             };
//         };
//
//
//
//
//         Principal.identity().then(function(account) {
//             vm.settingsAccount = copyAccount(account);
//         });
//
//          function save () {
//              vm.firstName = 'saeed';
//           }
//
//
//         function loadPersonByID(id)
//         {
//
//             // vm.personInfoDTO=  PersonInfo.get({id : id});
//             debugger;
//             vm.personInfoDTOlist= PersonInfoQuery.findByFirstName('fff').query().$promise;
//             //vm.myName= PersonInfoQuery.findByFirstName('fff').query();
//             vm.personInfoDTO = vm.personInfoDTOlist[0];
//
//             //setTimeout(500);
//             //console.log(vm.myName);
//
//         }
//
//         function loadPersonClick  () {
//             loadPersonByID(vm.accid)
//         }
//
//         //     Auth.updateAccount(vm.settingsAccount).then(function() {
//         //         vm.error = null;
//         //         vm.success = 'OK';
//         //         Principal.identity(true).then(function(account) {
//         //             vm.settingsAccount = copyAccount(account);
//         //         });
//         //         JhiLanguageService.getCurrent().then(function(current) {
//         //             if (vm.settingsAccount.langKey !== current) {
//         //                 $translate.use(vm.settingsAccount.langKey);
//         //             }
//         //         });
//         //     }).catch(function() {
//         //         vm.success = null;
//         //         vm.error = 'ERROR';
//         //     });
//         // }
//     }
// })();
