(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('theme-setting-info', {
            parent: 'entity',
            url: '/theme-setting-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.themeSettingInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/theme-setting-info/theme-setting-infos.html',
                    controller: 'ThemeSettingInfoController',
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
                    $translatePartialLoader.addPart('themeSettingInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('theme-setting-info-detail', {
            parent: 'entity',
            url: '/theme-setting-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.themeSettingInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/theme-setting-info/theme-setting-info-detail.html',
                    controller: 'ThemeSettingInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('themeSettingInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ThemeSettingInfo', function($stateParams, ThemeSettingInfo) {
                    return ThemeSettingInfo.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('theme-setting-info.new', {
            parent: 'theme-setting-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/theme-setting-info/theme-setting-info-dialog.html',
                    controller: 'ThemeSettingInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('theme-setting-info', null, { reload: true });
                }, function() {
                    $state.go('theme-setting-info');
                });
            }]
        })
        .state('theme-setting-info.edit', {
            parent: 'theme-setting-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/theme-setting-info/theme-setting-info-dialog.html',
                    controller: 'ThemeSettingInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ThemeSettingInfo', function(ThemeSettingInfo) {
                            return ThemeSettingInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('theme-setting-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('theme-setting-info.delete', {
            parent: 'theme-setting-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/theme-setting-info/theme-setting-info-delete-dialog.html',
                    controller: 'ThemeSettingInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ThemeSettingInfo', function(ThemeSettingInfo) {
                            return ThemeSettingInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('theme-setting-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
