'use strict';

describe('Controller Tests', function() {

    describe('CustomerRank Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCustomerRank, MockCustomer, MockServiceItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCustomerRank = jasmine.createSpy('MockCustomerRank');
            MockCustomer = jasmine.createSpy('MockCustomer');
            MockServiceItem = jasmine.createSpy('MockServiceItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CustomerRank': MockCustomerRank,
                'Customer': MockCustomer,
                'ServiceItem': MockServiceItem
            };
            createController = function() {
                $injector.get('$controller')("CustomerRankDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:customerRankUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
