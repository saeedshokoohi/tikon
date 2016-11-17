(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('social-network-info', {
            parent: 'entity',
            url: '/social-network-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.socialNetworkInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/social-network-info/social-network-infos.html',
                    controller: 'SocialNetworkInfoController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('socialNetworkInfo');
                    $translatePartialLoader.addPart('socialNetworkType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('social-network-info-detail', {
            parent: 'entity',
            url: '/social-network-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.socialNetworkInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/social-network-info/social-network-info-detail.html',
                    controller: 'SocialNetworkInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('socialNetworkInfo');
                    $translatePartialLoader.addPart('socialNetworkType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SocialNetworkInfo', function($stateParams, SocialNetworkInfo) {
                    return SocialNetworkInfo.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('social-network-info.new', {
            parent: 'social-network-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/social-network-info/social-network-info-dialog.html',
                    controller: 'SocialNetworkInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                socialNetworkType: null,
                                title: null,
                                url: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('social-network-info', null, { reload: true });
                }, function() {
                    $state.go('social-network-info');
                });
            }]
        })
        .state('social-network-info.edit', {
            parent: 'social-network-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/social-network-info/social-network-info-dialog.html',
                    controller: 'SocialNetworkInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SocialNetworkInfo', function(SocialNetworkInfo) {
                            return SocialNetworkInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('social-network-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('social-network-info.delete', {
            parent: 'social-network-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/social-network-info/social-network-info-delete-dialog.html',
                    controller: 'SocialNetworkInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SocialNetworkInfo', function(SocialNetworkInfo) {
                            return SocialNetworkInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('social-network-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
