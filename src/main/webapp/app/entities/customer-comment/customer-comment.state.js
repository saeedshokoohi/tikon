(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('customer-comment', {
            parent: 'entity',
            url: '/customer-comment?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.customerComment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-comment/customer-comments.html',
                    controller: 'CustomerCommentController',
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
                    $translatePartialLoader.addPart('customerComment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('customer-comment-detail', {
            parent: 'entity',
            url: '/customer-comment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.customerComment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-comment/customer-comment-detail.html',
                    controller: 'CustomerCommentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customerComment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CustomerComment', function($stateParams, CustomerComment) {
                    return CustomerComment.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('customer-comment.new', {
            parent: 'customer-comment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-comment/customer-comment-dialog.html',
                    controller: 'CustomerCommentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createDate: null,
                                commentText: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('customer-comment', null, { reload: true });
                }, function() {
                    $state.go('customer-comment');
                });
            }]
        })
        .state('customer-comment.edit', {
            parent: 'customer-comment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-comment/customer-comment-dialog.html',
                    controller: 'CustomerCommentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CustomerComment', function(CustomerComment) {
                            return CustomerComment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer-comment', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('customer-comment.delete', {
            parent: 'customer-comment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-comment/customer-comment-delete-dialog.html',
                    controller: 'CustomerCommentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CustomerComment', function(CustomerComment) {
                            return CustomerComment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer-comment', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
