'use strict';

describe('Controller Tests', function() {

    describe('OrderBagServiceItemDtail Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOrderBagServiceItemDtail, MockOrderBagServiceItem, MockPriceInfoDtail;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOrderBagServiceItemDtail = jasmine.createSpy('MockOrderBagServiceItemDtail');
            MockOrderBagServiceItem = jasmine.createSpy('MockOrderBagServiceItem');
            MockPriceInfoDtail = jasmine.createSpy('MockPriceInfoDtail');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'OrderBagServiceItemDtail': MockOrderBagServiceItemDtail,
                'OrderBagServiceItem': MockOrderBagServiceItem,
                'PriceInfoDtail': MockPriceInfoDtail
            };
            createController = function() {
                $injector.get('$controller')("OrderBagServiceItemDtailDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:orderBagServiceItemDtailUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
