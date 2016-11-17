(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('location-info', {
            parent: 'entity',
            url: '/location-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.locationInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/location-info/location-infos.html',
                    controller: 'LocationInfoController',
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
                    $translatePartialLoader.addPart('locationInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('location-info-detail', {
            parent: 'entity',
            url: '/location-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.locationInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/location-info/location-info-detail.html',
                    controller: 'LocationInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('locationInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LocationInfo', function($stateParams, LocationInfo) {
                    return LocationInfo.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('location-info.new', {
            parent: 'location-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/location-info/location-info-dialog.html',
                    controller: 'LocationInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                address: null,
                                mapX: null,
                                mapY: null,
                                isActive: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('location-info', null, { reload: true });
                }, function() {
                    $state.go('location-info');
                });
            }]
        })
        .state('location-info.edit', {
            parent: 'location-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/location-info/location-info-dialog.html',
                    controller: 'LocationInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LocationInfo', function(LocationInfo) {
                            return LocationInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('location-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('location-info.delete', {
            parent: 'location-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/location-info/location-info-delete-dialog.html',
                    controller: 'LocationInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LocationInfo', function(LocationInfo) {
                            return LocationInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('location-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
