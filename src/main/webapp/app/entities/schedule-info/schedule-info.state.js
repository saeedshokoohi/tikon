(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('schedule-info', {
            parent: 'entity',
            url: '/schedule-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.scheduleInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/schedule-info/schedule-infos.html',
                    controller: 'ScheduleInfoController',
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
                    $translatePartialLoader.addPart('scheduleInfo');
                    $translatePartialLoader.addPart('scheduleType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('schedule-info-detail', {
            parent: 'entity',
            url: '/schedule-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.scheduleInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/schedule-info/schedule-info-detail.html',
                    controller: 'ScheduleInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('scheduleInfo');
                    $translatePartialLoader.addPart('scheduleType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ScheduleInfo', function($stateParams, ScheduleInfo) {
                    return ScheduleInfo.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('schedule-info.new', {
            parent: 'schedule-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/schedule-info/schedule-info-dialog.html',
                    controller: 'ScheduleInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                scheduleType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('schedule-info', null, { reload: true });
                }, function() {
                    $state.go('schedule-info');
                });
            }]
        })
        .state('schedule-info.edit', {
            parent: 'schedule-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/schedule-info/schedule-info-dialog.html',
                    controller: 'ScheduleInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ScheduleInfo', function(ScheduleInfo) {
                            return ScheduleInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('schedule-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('schedule-info.delete', {
            parent: 'schedule-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/schedule-info/schedule-info-delete-dialog.html',
                    controller: 'ScheduleInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ScheduleInfo', function(ScheduleInfo) {
                            return ScheduleInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('schedule-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
