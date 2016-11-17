(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('createAccount', {
            parent: 'app',
            url: '/createAccount',
            data: {
                authorities: [],
                pageTitle: 'global.menu.account.create-account'
            },
            views: {
                'content@': {
                    templateUrl: 'app/createAccount/create-account.html',
                    controller: 'CreateAccountController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('register');
                    return $translate.refresh();
                }]
            }
        })
        .state('createAccount.personInfo', {
            parent: 'createAccount',
            url: '/PersonInfo',
            data: {
                authorities: []
            },
            views: {
                'subcontent2': {
                    templateUrl: 'app/entities/person-info/person-info-dialog.html',
                    controller: 'PersonInfoDialogControllerCopy',
                    controllerAs: 'vm'
                },
                'subcontent1': {
                    templateUrl: 'app/account/register/register.html',
                    controller: 'RegisterController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('register');
                    return $translate.refresh();
                }],
                entity: function () {
                    return {
                        nationalCode: null,
                        fisrtName: null,
                        lastName: null,
                        gender: null,
                        phoneNumber: null,
                        birthDate: null,
                        id: null
                    };
                }
            }
        })
            // .state('createAccount.Register', {
            //     parent: 'createAccount',
            //     url: '/userAccount/register',
            //     data: {
            //         authorities: []
            //     },
            //     views: {
            //         'subcontent1': {
            //             templateUrl: 'app/account/register/register.html',
            //             controller: 'RegisterController',
            //             controllerAs: 'vm'
            //         }
            //     },
            //     resolve: {
            //         translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            //             $translatePartialLoader.addPart('register');
            //             return $translate.refresh();
            //         }],
            //         // entity: ['$stateParams', 'Register', function ($stateParams, Register) {
            //         //     return Register.get({id: $stateParams.id}).$promise;
            //         // }]
            //     }
            // })



        ;

    }
})();
