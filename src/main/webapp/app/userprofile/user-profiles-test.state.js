// (function() {
//     'use strict';
//
//     angular
//         .module('tikonApp')
//         .config(stateConfig);
//
//     stateConfig.$inject = ['$stateProvider'];
//
//     function stateConfig($stateProvider) {
//         $stateProvider.state('userProfile', {
//             parent: 'app',
//             url: '/userProfile',
//             data: {
//                 authorities: ['ROLE_USER'],
//                 pageTitle: 'global.menu.account.user-profile'
//             },
//             views: {
//                 'content@': {
//                     templateUrl: 'app/userprofile/user-profiles.html',
//                     controller: 'UserProfileController',
//                     controllerAs: 'vm'
//                 }
//             },
//             resolve: {
//                 translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
//                     $translatePartialLoader.addPart('personInfo');
//                     return $translate.refresh();
//                 }]
//             }
//
//         })
//
//             .state('userProfile.editPerson', {
//                 parent: 'userProfile',
//                 url: '/{id}/editPerson',
//                 data: {
//                     authorities: ['ROLE_USER']
//                 },
//                 onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
//                     $uibModal.open({
//
//                         templateUrl: 'app/entities/person-info/person-info-dialog.html',
//                         controller: 'PersonInfoDialogController',
//                         controllerAs: 'vm',
//                         backdrop: 'static',
//                         size: 'lg',
//                         resolve: {
//                             entity: ['PersonInfo', function(PersonInfo) {
//                                 return PersonInfo.get({id : $stateParams.id}).$promise;
//                             }]
//                         }
//                     }).result.then(function() {
//                         $state.go('userProfile', null, { reload: true });
//                     }, function() {
//                         $state.go('^');
//                     });
//                 }]
//             })
//
//
//
//         ;
//     }
// })();
