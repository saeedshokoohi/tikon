(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('order-bag', {
            parent: 'entity',
            url: '/order-bag?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.orderBag.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/order-bag/order-bags.html',
                    controller: 'OrderBagController',
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
                    $translatePartialLoader.addPart('orderBag');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('order-bag-detail', {
            parent: 'entity',
            url: '/order-bag/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.orderBag.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/order-bag/order-bag-detail.html',
                    controller: 'OrderBagDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('orderBag');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'OrderBag', function($stateParams, OrderBag) {
                    return OrderBag.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('order-bag.new', {
            parent: 'order-bag',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-bag/order-bag-dialog.html',
                    controller: 'OrderBagDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createDate: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('order-bag', null, { reload: true });
                }, function() {
                    $state.go('order-bag');
                });
            }]
        })
        .state('order-bag.edit', {
            parent: 'order-bag',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-bag/order-bag-dialog.html',
                    controller: 'OrderBagDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OrderBag', function(OrderBag) {
                            return OrderBag.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-bag', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('order-bag.delete', {
            parent: 'order-bag',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-bag/order-bag-delete-dialog.html',
                    controller: 'OrderBagDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OrderBag', function(OrderBag) {
                            return OrderBag.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-bag', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
