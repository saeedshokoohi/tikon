(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('userProfile', {
            parent: 'app',
            url: '/userProfile',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'global.menu.account.user-profile'
            },
            views: {
                'content@': {
                    templateUrl: 'app/userprofile/user-profiles.html',
                    controller: 'UserProfileController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userProfile');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }

        })


        ;
    }

})();
