(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('payment-log', {
            parent: 'entity',
            url: '/payment-log?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.paymentLog.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment-log/payment-logs.html',
                    controller: 'PaymentLogController',
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
                    $translatePartialLoader.addPart('paymentLog');
                    $translatePartialLoader.addPart('paymentType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('payment-log-detail', {
            parent: 'entity',
            url: '/payment-log/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.paymentLog.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment-log/payment-log-detail.html',
                    controller: 'PaymentLogDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('paymentLog');
                    $translatePartialLoader.addPart('paymentType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PaymentLog', function($stateParams, PaymentLog) {
                    return PaymentLog.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('payment-log.new', {
            parent: 'payment-log',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-log/payment-log-dialog.html',
                    controller: 'PaymentLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                traceCode: null,
                                paymentType: null,
                                createDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('payment-log', null, { reload: true });
                }, function() {
                    $state.go('payment-log');
                });
            }]
        })
        .state('payment-log.edit', {
            parent: 'payment-log',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-log/payment-log-dialog.html',
                    controller: 'PaymentLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaymentLog', function(PaymentLog) {
                            return PaymentLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-log', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment-log.delete', {
            parent: 'payment-log',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-log/payment-log-delete-dialog.html',
                    controller: 'PaymentLogDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PaymentLog', function(PaymentLog) {
                            return PaymentLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-log', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
