(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('participant-person', {
            parent: 'entity',
            url: '/participant-person?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.participantPerson.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/participant-person/participant-people.html',
                    controller: 'ParticipantPersonController',
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
                    $translatePartialLoader.addPart('participantPerson');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('participant-person-detail', {
            parent: 'entity',
            url: '/participant-person/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.participantPerson.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/participant-person/participant-person-detail.html',
                    controller: 'ParticipantPersonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('participantPerson');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ParticipantPerson', function($stateParams, ParticipantPerson) {
                    return ParticipantPerson.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('participant-person.new', {
            parent: 'participant-person',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/participant-person/participant-person-dialog.html',
                    controller: 'ParticipantPersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('participant-person', null, { reload: true });
                }, function() {
                    $state.go('participant-person');
                });
            }]
        })
        .state('participant-person.edit', {
            parent: 'participant-person',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/participant-person/participant-person-dialog.html',
                    controller: 'ParticipantPersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ParticipantPerson', function(ParticipantPerson) {
                            return ParticipantPerson.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('participant-person', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('participant-person.delete', {
            parent: 'participant-person',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/participant-person/participant-person-delete-dialog.html',
                    controller: 'ParticipantPersonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ParticipantPerson', function(ParticipantPerson) {
                            return ParticipantPerson.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('participant-person', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
