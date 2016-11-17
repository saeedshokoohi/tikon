(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('createUserAccount', {
            parent: 'app',
            url: '/createUserAccount',
            data: {
                authorities: [],
                pageTitle: 'register.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/createUserAccount/create-user-account.html',
                    controller: 'CreateUserAccountController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('register');
                    $translatePartialLoader.addPart('personInfo');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
