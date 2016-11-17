(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('time-period', {
            parent: 'entity',
            url: '/time-period?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.timePeriod.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/time-period/time-periods.html',
                    controller: 'TimePeriodController',
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
                    $translatePartialLoader.addPart('timePeriod');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('time-period-detail', {
            parent: 'entity',
            url: '/time-period/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.timePeriod.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/time-period/time-period-detail.html',
                    controller: 'TimePeriodDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('timePeriod');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TimePeriod', function($stateParams, TimePeriod) {
                    return TimePeriod.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('time-period.new', {
            parent: 'time-period',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/time-period/time-period-dialog.html',
                    controller: 'TimePeriodDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startTime: null,
                                endTime: null,
                                duration: null,
                                gaptime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('time-period', null, { reload: true });
                }, function() {
                    $state.go('time-period');
                });
            }]
        })
        .state('time-period.edit', {
            parent: 'time-period',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/time-period/time-period-dialog.html',
                    controller: 'TimePeriodDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TimePeriod', function(TimePeriod) {
                            return TimePeriod.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('time-period', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('time-period.delete', {
            parent: 'time-period',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/time-period/time-period-delete-dialog.html',
                    controller: 'TimePeriodDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TimePeriod', function(TimePeriod) {
                            return TimePeriod.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('time-period', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
