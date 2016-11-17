(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('order-bag-service-item-dtail', {
            parent: 'entity',
            url: '/order-bag-service-item-dtail?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.orderBagServiceItemDtail.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/order-bag-service-item-dtail/order-bag-service-item-dtails.html',
                    controller: 'OrderBagServiceItemDtailController',
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
                    $translatePartialLoader.addPart('orderBagServiceItemDtail');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('order-bag-service-item-dtail-detail', {
            parent: 'entity',
            url: '/order-bag-service-item-dtail/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.orderBagServiceItemDtail.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/order-bag-service-item-dtail/order-bag-service-item-dtail-detail.html',
                    controller: 'OrderBagServiceItemDtailDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('orderBagServiceItemDtail');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'OrderBagServiceItemDtail', function($stateParams, OrderBagServiceItemDtail) {
                    return OrderBagServiceItemDtail.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('order-bag-service-item-dtail.new', {
            parent: 'order-bag-service-item-dtail',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-bag-service-item-dtail/order-bag-service-item-dtail-dialog.html',
                    controller: 'OrderBagServiceItemDtailDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                reserveTime: null,
                                gty: null,
                                price: null,
                                discount: null,
                                vat: null,
                                totalPrice: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('order-bag-service-item-dtail', null, { reload: true });
                }, function() {
                    $state.go('order-bag-service-item-dtail');
                });
            }]
        })
        .state('order-bag-service-item-dtail.edit', {
            parent: 'order-bag-service-item-dtail',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-bag-service-item-dtail/order-bag-service-item-dtail-dialog.html',
                    controller: 'OrderBagServiceItemDtailDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OrderBagServiceItemDtail', function(OrderBagServiceItemDtail) {
                            return OrderBagServiceItemDtail.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-bag-service-item-dtail', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('order-bag-service-item-dtail.delete', {
            parent: 'order-bag-service-item-dtail',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-bag-service-item-dtail/order-bag-service-item-dtail-delete-dialog.html',
                    controller: 'OrderBagServiceItemDtailDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OrderBagServiceItemDtail', function(OrderBagServiceItemDtail) {
                            return OrderBagServiceItemDtail.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-bag-service-item-dtail', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
