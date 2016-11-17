'use strict';

describe('Controller Tests', function() {

    describe('OrderBagServiceItem Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOrderBagServiceItem, MockOrderBag, MockServiceItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOrderBagServiceItem = jasmine.createSpy('MockOrderBagServiceItem');
            MockOrderBag = jasmine.createSpy('MockOrderBag');
            MockServiceItem = jasmine.createSpy('MockServiceItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'OrderBagServiceItem': MockOrderBagServiceItem,
                'OrderBag': MockOrderBag,
                'ServiceItem': MockServiceItem
            };
            createController = function() {
                $injector.get('$controller')("OrderBagServiceItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:orderBagServiceItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
