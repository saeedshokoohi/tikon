(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('album-info', {
            parent: 'entity',
            url: '/album-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.albumInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/album-info/album-infos.html',
                    controller: 'AlbumInfoController',
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
                    $translatePartialLoader.addPart('albumInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('album-info-detail', {
            parent: 'entity',
            url: '/album-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.albumInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/album-info/album-info-detail.html',
                    controller: 'AlbumInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('albumInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AlbumInfo', function($stateParams, AlbumInfo) {
                    return AlbumInfo.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('album-info.new', {
            parent: 'album-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/album-info/album-info-dialog.html',
                    controller: 'AlbumInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                isSingleImage: null,
                                caption: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('album-info', null, { reload: true });
                }, function() {
                    $state.go('album-info');
                });
            }]
        })
        .state('album-info.edit', {
            parent: 'album-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/album-info/album-info-dialog.html',
                    controller: 'AlbumInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AlbumInfo', function(AlbumInfo) {
                            return AlbumInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('album-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('album-info.delete', {
            parent: 'album-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/album-info/album-info-delete-dialog.html',
                    controller: 'AlbumInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AlbumInfo', function(AlbumInfo) {
                            return AlbumInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('album-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
