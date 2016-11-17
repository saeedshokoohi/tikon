(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('capacity-exception', {
            parent: 'entity',
            url: '/capacity-exception?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.capacityException.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/capacity-exception/capacity-exceptions.html',
                    controller: 'CapacityExceptionController',
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
                    $translatePartialLoader.addPart('capacityException');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('capacity-exception-detail', {
            parent: 'entity',
            url: '/capacity-exception/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.capacityException.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/capacity-exception/capacity-exception-detail.html',
                    controller: 'CapacityExceptionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('capacityException');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CapacityException', function($stateParams, CapacityException) {
                    return CapacityException.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('capacity-exception.new', {
            parent: 'capacity-exception',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/capacity-exception/capacity-exception-dialog.html',
                    controller: 'CapacityExceptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                exceptionType: null,
                                reserveTime: null,
                                newQty: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('capacity-exception', null, { reload: true });
                }, function() {
                    $state.go('capacity-exception');
                });
            }]
        })
        .state('capacity-exception.edit', {
            parent: 'capacity-exception',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/capacity-exception/capacity-exception-dialog.html',
                    controller: 'CapacityExceptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CapacityException', function(CapacityException) {
                            return CapacityException.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('capacity-exception', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('capacity-exception.delete', {
            parent: 'capacity-exception',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/capacity-exception/capacity-exception-delete-dialog.html',
                    controller: 'CapacityExceptionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CapacityException', function(CapacityException) {
                            return CapacityException.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('capacity-exception', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
