(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('servant', {
            parent: 'entity',
            url: '/servant?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.servant.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/servant/servants.html',
                    controller: 'ServantController',
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
                    $translatePartialLoader.addPart('servant');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('servant-detail', {
            parent: 'entity',
            url: '/servant/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.servant.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/servant/servant-detail.html',
                    controller: 'ServantDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('servant');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Servant', function($stateParams, Servant) {
                    return Servant.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('servant.new', {
            parent: 'servant',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/servant/servant-dialog.html',
                    controller: 'ServantDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                level: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('servant', null, { reload: true });
                }, function() {
                    $state.go('servant');
                });
            }]
        })
        .state('servant.edit', {
            parent: 'servant',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/servant/servant-dialog.html',
                    controller: 'ServantDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Servant', function(Servant) {
                            return Servant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('servant', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('servant.delete', {
            parent: 'servant',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/servant/servant-delete-dialog.html',
                    controller: 'ServantDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Servant', function(Servant) {
                            return Servant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('servant', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
