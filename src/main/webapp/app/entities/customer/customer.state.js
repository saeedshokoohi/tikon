(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('customer', {
            parent: 'entity',
            url: '/customer?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.customer.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer/customers.html',
                    controller: 'CustomerController',
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
                    $translatePartialLoader.addPart('customer');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('customer-detail', {
            parent: 'entity',
            url: '/customer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.customer.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer/customer-detail.html',
                    controller: 'CustomerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customer');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Customer', function($stateParams, Customer) {
                    return Customer.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('customer.new', {
            parent: 'customer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer/customer-dialog.html',
                    controller: 'CustomerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userAccountId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('customer', null, { reload: true });
                }, function() {
                    $state.go('customer');
                });
            }]
        })
        .state('customer.edit', {
            parent: 'customer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer/customer-dialog.html',
                    controller: 'CustomerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Customer', function(Customer) {
                            return Customer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('customer.delete', {
            parent: 'customer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer/customer-delete-dialog.html',
                    controller: 'CustomerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Customer', function(Customer) {
                            return Customer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
