(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('selector-data-type', {
            parent: 'entity',
            url: '/selector-data-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.selectorDataType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/selector-data-type/selector-data-types.html',
                    controller: 'SelectorDataTypeController',
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
                    $translatePartialLoader.addPart('selectorDataType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('selector-data-type-detail', {
            parent: 'entity',
            url: '/selector-data-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.selectorDataType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/selector-data-type/selector-data-type-detail.html',
                    controller: 'SelectorDataTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('selectorDataType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SelectorDataType', function($stateParams, SelectorDataType) {
                    return SelectorDataType.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('selector-data-type.new', {
            parent: 'selector-data-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/selector-data-type/selector-data-type-dialog.html',
                    controller: 'SelectorDataTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                key: null,
                                typeName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('selector-data-type', null, { reload: true });
                }, function() {
                    $state.go('selector-data-type');
                });
            }]
        })
        .state('selector-data-type.edit', {
            parent: 'selector-data-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/selector-data-type/selector-data-type-dialog.html',
                    controller: 'SelectorDataTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SelectorDataType', function(SelectorDataType) {
                            return SelectorDataType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('selector-data-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('selector-data-type.delete', {
            parent: 'selector-data-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/selector-data-type/selector-data-type-delete-dialog.html',
                    controller: 'SelectorDataTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SelectorDataType', function(SelectorDataType) {
                            return SelectorDataType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('selector-data-type', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
