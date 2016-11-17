(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('image-data', {
            parent: 'entity',
            url: '/image-data?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.imageData.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/image-data/image-data.html',
                    controller: 'ImageDataController',
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
                    $translatePartialLoader.addPart('imageData');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('image-data-detail', {
            parent: 'entity',
            url: '/image-data/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.imageData.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/image-data/image-data-detail.html',
                    controller: 'ImageDataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('imageData');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ImageData', function($stateParams, ImageData) {
                    return ImageData.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('image-data.new', {
            parent: 'image-data',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/image-data/image-data-dialog.html',
                    controller: 'ImageDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fileName: null,
                                caption: null,
                                fileType: null,
                                fileData: null,
                                fileDataContentType: null,
                                thumbnailData: null,
                                thumbnailDataContentType: null,
                                isCoverImage: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('image-data', null, { reload: true });
                }, function() {
                    $state.go('image-data');
                });
            }]
        })
        .state('image-data.edit', {
            parent: 'image-data',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/image-data/image-data-dialog.html',
                    controller: 'ImageDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ImageData', function(ImageData) {
                            return ImageData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('image-data', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('image-data.delete', {
            parent: 'image-data',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/image-data/image-data-delete-dialog.html',
                    controller: 'ImageDataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ImageData', function(ImageData) {
                            return ImageData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('image-data', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
