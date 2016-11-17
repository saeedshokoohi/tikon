(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('off-time', {
            parent: 'entity',
            url: '/off-time?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.offTime.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/off-time/off-times.html',
                    controller: 'OffTimeController',
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
                    $translatePartialLoader.addPart('offTime');
                    $translatePartialLoader.addPart('offTimeType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('off-time-detail', {
            parent: 'entity',
            url: '/off-time/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.offTime.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/off-time/off-time-detail.html',
                    controller: 'OffTimeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('offTime');
                    $translatePartialLoader.addPart('offTimeType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'OffTime', function($stateParams, OffTime) {
                    return OffTime.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('off-time.new', {
            parent: 'off-time',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/off-time/off-time-dialog.html',
                    controller: 'OffTimeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                i: null,
                                fromTime: null,
                                toTime: null,
                                offTimeType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('off-time', null, { reload: true });
                }, function() {
                    $state.go('off-time');
                });
            }]
        })
        .state('off-time.edit', {
            parent: 'off-time',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/off-time/off-time-dialog.html',
                    controller: 'OffTimeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OffTime', function(OffTime) {
                            return OffTime.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('off-time', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('off-time.delete', {
            parent: 'off-time',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/off-time/off-time-delete-dialog.html',
                    controller: 'OffTimeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OffTime', function(OffTime) {
                            return OffTime.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('off-time', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
