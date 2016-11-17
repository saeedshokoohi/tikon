'use strict';

describe('Controller Tests', function() {

    describe('CustomerComment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCustomerComment, MockCustomer, MockServiceItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCustomerComment = jasmine.createSpy('MockCustomerComment');
            MockCustomer = jasmine.createSpy('MockCustomer');
            MockServiceItem = jasmine.createSpy('MockServiceItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CustomerComment': MockCustomerComment,
                'Customer': MockCustomer,
                'ServiceItem': MockServiceItem
            };
            createController = function() {
                $injector.get('$controller')("CustomerCommentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:customerCommentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
