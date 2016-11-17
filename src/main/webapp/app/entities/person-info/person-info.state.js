(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('person-info', {
            parent: 'entity',
            url: '/person-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.personInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/person-info/person-infos.html',
                    controller: 'PersonInfoController',
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
                    $translatePartialLoader.addPart('personInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('person-info-detail', {
            parent: 'entity',
            url: '/person-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.personInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/person-info/person-info-detail.html',
                    controller: 'PersonInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('personInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PersonInfo', function($stateParams, PersonInfo) {
                    return PersonInfo.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('person-info.new', {
            parent: 'person-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/person-info/person-info-dialog.html',
                    controller: 'PersonInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nationalCode: null,
                                fisrtName: null,
                                lastName: null,
                                gender: null,
                                phoneNumber: null,
                                birthDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('person-info', null, { reload: true });
                }, function() {
                    $state.go('person-info');
                });
            }]
        })
        .state('person-info.edit', {
            parent: 'person-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/person-info/person-info-dialog.html',
                    controller: 'PersonInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PersonInfo', function(PersonInfo) {
                            return PersonInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('person-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('person-info.delete', {
            parent: 'person-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/person-info/person-info-delete-dialog.html',
                    controller: 'PersonInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PersonInfo', function(PersonInfo) {
                            return PersonInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('person-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
