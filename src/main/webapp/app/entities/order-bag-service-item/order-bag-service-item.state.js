(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('order-bag-service-item', {
            parent: 'entity',
            url: '/order-bag-service-item?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.orderBagServiceItem.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/order-bag-service-item/order-bag-service-items.html',
                    controller: 'OrderBagServiceItemController',
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
                    $translatePartialLoader.addPart('orderBagServiceItem');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('order-bag-service-item-detail', {
            parent: 'entity',
            url: '/order-bag-service-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.orderBagServiceItem.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/order-bag-service-item/order-bag-service-item-detail.html',
                    controller: 'OrderBagServiceItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('orderBagServiceItem');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'OrderBagServiceItem', function($stateParams, OrderBagServiceItem) {
                    return OrderBagServiceItem.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('order-bag-service-item.new', {
            parent: 'order-bag-service-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-bag-service-item/order-bag-service-item-dialog.html',
                    controller: 'OrderBagServiceItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                subtotalServicePrice: null,
                                subtotalOptionPrice: null,
                                subtotalVAT: null,
                                subtotalDiscount: null,
                                totalRowPrice: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('order-bag-service-item', null, { reload: true });
                }, function() {
                    $state.go('order-bag-service-item');
                });
            }]
        })
        .state('order-bag-service-item.edit', {
            parent: 'order-bag-service-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-bag-service-item/order-bag-service-item-dialog.html',
                    controller: 'OrderBagServiceItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OrderBagServiceItem', function(OrderBagServiceItem) {
                            return OrderBagServiceItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-bag-service-item', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('order-bag-service-item.delete', {
            parent: 'order-bag-service-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-bag-service-item/order-bag-service-item-delete-dialog.html',
                    controller: 'OrderBagServiceItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OrderBagServiceItem', function(OrderBagServiceItem) {
                            return OrderBagServiceItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-bag-service-item', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
