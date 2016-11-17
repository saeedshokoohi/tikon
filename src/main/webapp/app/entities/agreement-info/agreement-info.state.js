(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('agreement-info', {
            parent: 'entity',
            url: '/agreement-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.agreementInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agreement-info/agreement-infos.html',
                    controller: 'AgreementInfoController',
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
                    $translatePartialLoader.addPart('agreementInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('agreement-info-detail', {
            parent: 'entity',
            url: '/agreement-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.agreementInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agreement-info/agreement-info-detail.html',
                    controller: 'AgreementInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('agreementInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AgreementInfo', function($stateParams, AgreementInfo) {
                    return AgreementInfo.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('agreement-info.new', {
            parent: 'agreement-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agreement-info/agreement-info-dialog.html',
                    controller: 'AgreementInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                rules: null,
                                agreement: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('agreement-info', null, { reload: true });
                }, function() {
                    $state.go('agreement-info');
                });
            }]
        })
        .state('agreement-info.edit', {
            parent: 'agreement-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agreement-info/agreement-info-dialog.html',
                    controller: 'AgreementInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AgreementInfo', function(AgreementInfo) {
                            return AgreementInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agreement-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('agreement-info.delete', {
            parent: 'agreement-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agreement-info/agreement-info-delete-dialog.html',
                    controller: 'AgreementInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AgreementInfo', function(AgreementInfo) {
                            return AgreementInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agreement-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
