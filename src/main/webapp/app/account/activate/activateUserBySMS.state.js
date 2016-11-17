(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {

        $stateProvider
           .state('activateUserBySMS', {

                parent: 'account',
                url: '/activateUserBySMS/{login}',
                data: {
                    authorities: [],
                    pageTitle: 'activate.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/account/activate/activateUserBySMS.html',
                        controller: 'ActivationBySMSController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('activate');
                        return $translate.refresh();
                    }]
                    ,entity: ['$stateParams', 'User', function($stateParams, User) {
                        debugger;
                        return User.get({login : $stateParams.login}).$promise;
                    }]
                }

            })


        ;
    }
})();
