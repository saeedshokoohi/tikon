(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('meta-tag', {
            parent: 'entity',
            url: '/meta-tag?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.metaTag.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/meta-tag/meta-tags.html',
                    controller: 'MetaTagController',
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
                    $translatePartialLoader.addPart('metaTag');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('meta-tag-detail', {
            parent: 'entity',
            url: '/meta-tag/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.metaTag.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/meta-tag/meta-tag-detail.html',
                    controller: 'MetaTagDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('metaTag');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MetaTag', function($stateParams, MetaTag) {
                    return MetaTag.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('meta-tag.new', {
            parent: 'meta-tag',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meta-tag/meta-tag-dialog.html',
                    controller: 'MetaTagDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                tag: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('meta-tag', null, { reload: true });
                }, function() {
                    $state.go('meta-tag');
                });
            }]
        })
        .state('meta-tag.edit', {
            parent: 'meta-tag',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meta-tag/meta-tag-dialog.html',
                    controller: 'MetaTagDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MetaTag', function(MetaTag) {
                            return MetaTag.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('meta-tag', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('meta-tag.delete', {
            parent: 'meta-tag',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meta-tag/meta-tag-delete-dialog.html',
                    controller: 'MetaTagDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MetaTag', function(MetaTag) {
                            return MetaTag.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('meta-tag', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
