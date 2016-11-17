(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('date-period', {
            parent: 'entity',
            url: '/date-period?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.datePeriod.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/date-period/date-periods.html',
                    controller: 'DatePeriodController',
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
                    $translatePartialLoader.addPart('datePeriod');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('date-period-detail', {
            parent: 'entity',
            url: '/date-period/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.datePeriod.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/date-period/date-period-detail.html',
                    controller: 'DatePeriodDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('datePeriod');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DatePeriod', function($stateParams, DatePeriod) {
                    return DatePeriod.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('date-period.new', {
            parent: 'date-period',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/date-period/date-period-dialog.html',
                    controller: 'DatePeriodDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fromDate: null,
                                toDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('date-period', null, { reload: true });
                }, function() {
                    $state.go('date-period');
                });
            }]
        })
        .state('date-period.edit', {
            parent: 'date-period',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/date-period/date-period-dialog.html',
                    controller: 'DatePeriodDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DatePeriod', function(DatePeriod) {
                            return DatePeriod.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('date-period', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('date-period.delete', {
            parent: 'date-period',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/date-period/date-period-delete-dialog.html',
                    controller: 'DatePeriodDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DatePeriod', function(DatePeriod) {
                            return DatePeriod.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('date-period', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
