(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('off-day', {
            parent: 'entity',
            url: '/off-day?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.offDay.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/off-day/off-days.html',
                    controller: 'OffDayController',
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
                    $translatePartialLoader.addPart('offDay');
                    $translatePartialLoader.addPart('offDayType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('off-day-detail', {
            parent: 'entity',
            url: '/off-day/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.offDay.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/off-day/off-day-detail.html',
                    controller: 'OffDayDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('offDay');
                    $translatePartialLoader.addPart('offDayType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'OffDay', function($stateParams, OffDay) {
                    return OffDay.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('off-day.new', {
            parent: 'off-day',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/off-day/off-day-dialog.html',
                    controller: 'OffDayDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                offDate: null,
                                offDayType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('off-day', null, { reload: true });
                }, function() {
                    $state.go('off-day');
                });
            }]
        })
        .state('off-day.edit', {
            parent: 'off-day',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/off-day/off-day-dialog.html',
                    controller: 'OffDayDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OffDay', function(OffDay) {
                            return OffDay.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('off-day', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('off-day.delete', {
            parent: 'off-day',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/off-day/off-day-delete-dialog.html',
                    controller: 'OffDayDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OffDay', function(OffDay) {
                            return OffDay.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('off-day', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
