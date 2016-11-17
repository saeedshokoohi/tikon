(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('invoice-info', {
            parent: 'entity',
            url: '/invoice-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.invoiceInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/invoice-info/invoice-infos.html',
                    controller: 'InvoiceInfoController',
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
                    $translatePartialLoader.addPart('invoiceInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('invoice-info-detail', {
            parent: 'entity',
            url: '/invoice-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.invoiceInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/invoice-info/invoice-info-detail.html',
                    controller: 'InvoiceInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('invoiceInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InvoiceInfo', function($stateParams, InvoiceInfo) {
                    return InvoiceInfo.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('invoice-info.new', {
            parent: 'invoice-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invoice-info/invoice-info-dialog.html',
                    controller: 'InvoiceInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('invoice-info', null, { reload: true });
                }, function() {
                    $state.go('invoice-info');
                });
            }]
        })
        .state('invoice-info.edit', {
            parent: 'invoice-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invoice-info/invoice-info-dialog.html',
                    controller: 'InvoiceInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InvoiceInfo', function(InvoiceInfo) {
                            return InvoiceInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('invoice-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('invoice-info.delete', {
            parent: 'invoice-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invoice-info/invoice-info-delete-dialog.html',
                    controller: 'InvoiceInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InvoiceInfo', function(InvoiceInfo) {
                            return InvoiceInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('invoice-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
