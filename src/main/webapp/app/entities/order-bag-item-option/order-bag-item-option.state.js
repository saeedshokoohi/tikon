(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('order-bag-item-option', {
            parent: 'entity',
            url: '/order-bag-item-option?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.orderBagItemOption.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/order-bag-item-option/order-bag-item-options.html',
                    controller: 'OrderBagItemOptionController',
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
                    $translatePartialLoader.addPart('orderBagItemOption');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('order-bag-item-option-detail', {
            parent: 'entity',
            url: '/order-bag-item-option/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.orderBagItemOption.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/order-bag-item-option/order-bag-item-option-detail.html',
                    controller: 'OrderBagItemOptionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('orderBagItemOption');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'OrderBagItemOption', function($stateParams, OrderBagItemOption) {
                    return OrderBagItemOption.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('order-bag-item-option.new', {
            parent: 'order-bag-item-option',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-bag-item-option/order-bag-item-option-dialog.html',
                    controller: 'OrderBagItemOptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                reserveTime: null,
                                qty: null,
                                price: null,
                                discount: null,
                                vat: null,
                                totalPrice: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('order-bag-item-option', null, { reload: true });
                }, function() {
                    $state.go('order-bag-item-option');
                });
            }]
        })
        .state('order-bag-item-option.edit', {
            parent: 'order-bag-item-option',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-bag-item-option/order-bag-item-option-dialog.html',
                    controller: 'OrderBagItemOptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OrderBagItemOption', function(OrderBagItemOption) {
                            return OrderBagItemOption.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-bag-item-option', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('order-bag-item-option.delete', {
            parent: 'order-bag-item-option',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/order-bag-item-option/order-bag-item-option-delete-dialog.html',
                    controller: 'OrderBagItemOptionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OrderBagItemOption', function(OrderBagItemOption) {
                            return OrderBagItemOption.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('order-bag-item-option', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
