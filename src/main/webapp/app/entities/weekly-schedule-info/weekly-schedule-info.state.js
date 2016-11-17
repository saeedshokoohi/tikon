(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('weekly-schedule-info', {
            parent: 'entity',
            url: '/weekly-schedule-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.weeklyScheduleInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/weekly-schedule-info/weekly-schedule-infos.html',
                    controller: 'WeeklyScheduleInfoController',
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
                    $translatePartialLoader.addPart('weeklyScheduleInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('weekly-schedule-info-detail', {
            parent: 'entity',
            url: '/weekly-schedule-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.weeklyScheduleInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/weekly-schedule-info/weekly-schedule-info-detail.html',
                    controller: 'WeeklyScheduleInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('weeklyScheduleInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WeeklyScheduleInfo', function($stateParams, WeeklyScheduleInfo) {
                    return WeeklyScheduleInfo.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('weekly-schedule-info.new', {
            parent: 'weekly-schedule-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/weekly-schedule-info/weekly-schedule-info-dialog.html',
                    controller: 'WeeklyScheduleInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('weekly-schedule-info', null, { reload: true });
                }, function() {
                    $state.go('weekly-schedule-info');
                });
            }]
        })
        .state('weekly-schedule-info.edit', {
            parent: 'weekly-schedule-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/weekly-schedule-info/weekly-schedule-info-dialog.html',
                    controller: 'WeeklyScheduleInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WeeklyScheduleInfo', function(WeeklyScheduleInfo) {
                            return WeeklyScheduleInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('weekly-schedule-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('weekly-schedule-info.delete', {
            parent: 'weekly-schedule-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/weekly-schedule-info/weekly-schedule-info-delete-dialog.html',
                    controller: 'WeeklyScheduleInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WeeklyScheduleInfo', function(WeeklyScheduleInfo) {
                            return WeeklyScheduleInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('weekly-schedule-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
