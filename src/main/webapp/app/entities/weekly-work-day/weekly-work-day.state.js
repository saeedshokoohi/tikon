(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('weekly-work-day', {
            parent: 'entity',
            url: '/weekly-work-day?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.weeklyWorkDay.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/weekly-work-day/weekly-work-days.html',
                    controller: 'WeeklyWorkDayController',
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
                    $translatePartialLoader.addPart('weeklyWorkDay');
                    $translatePartialLoader.addPart('weekDay');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('weekly-work-day-detail', {
            parent: 'entity',
            url: '/weekly-work-day/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.weeklyWorkDay.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/weekly-work-day/weekly-work-day-detail.html',
                    controller: 'WeeklyWorkDayDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('weeklyWorkDay');
                    $translatePartialLoader.addPart('weekDay');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WeeklyWorkDay', function($stateParams, WeeklyWorkDay) {
                    return WeeklyWorkDay.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('weekly-work-day.new', {
            parent: 'weekly-work-day',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/weekly-work-day/weekly-work-day-dialog.html',
                    controller: 'WeeklyWorkDayDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                weekday: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('weekly-work-day', null, { reload: true });
                }, function() {
                    $state.go('weekly-work-day');
                });
            }]
        })
        .state('weekly-work-day.edit', {
            parent: 'weekly-work-day',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/weekly-work-day/weekly-work-day-dialog.html',
                    controller: 'WeeklyWorkDayDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WeeklyWorkDay', function(WeeklyWorkDay) {
                            return WeeklyWorkDay.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('weekly-work-day', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('weekly-work-day.delete', {
            parent: 'weekly-work-day',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/weekly-work-day/weekly-work-day-delete-dialog.html',
                    controller: 'WeeklyWorkDayDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WeeklyWorkDay', function(WeeklyWorkDay) {
                            return WeeklyWorkDay.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('weekly-work-day', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
