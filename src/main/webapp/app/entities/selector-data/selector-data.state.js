(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('selector-data', {
            parent: 'entity',
            url: '/selector-data?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.selectorData.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/selector-data/selector-data.html',
                    controller: 'SelectorDataController',
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
                    $translatePartialLoader.addPart('selectorData');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('selector-data-detail', {
            parent: 'entity',
            url: '/selector-data/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.selectorData.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/selector-data/selector-data-detail.html',
                    controller: 'SelectorDataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('selectorData');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SelectorData', function($stateParams, SelectorData) {
                    return SelectorData.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('selector-data.new', {
            parent: 'selector-data',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/selector-data/selector-data-dialog.html',
                    controller: 'SelectorDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                key: null,
                                text: null,
                                orderNo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('selector-data', null, { reload: true });
                }, function() {
                    $state.go('selector-data');
                });
            }]
        })
        .state('selector-data.edit', {
            parent: 'selector-data',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/selector-data/selector-data-dialog.html',
                    controller: 'SelectorDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SelectorData', function(SelectorData) {
                            return SelectorData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('selector-data', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('selector-data.delete', {
            parent: 'selector-data',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/selector-data/selector-data-delete-dialog.html',
                    controller: 'SelectorDataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SelectorData', function(SelectorData) {
                            return SelectorData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('selector-data', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
