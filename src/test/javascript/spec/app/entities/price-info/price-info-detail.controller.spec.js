'use strict';

describe('Controller Tests', function() {

    describe('PriceInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPriceInfo, MockServant;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPriceInfo = jasmine.createSpy('MockPriceInfo');
            MockServant = jasmine.createSpy('MockServant');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PriceInfo': MockPriceInfo,
                'Servant': MockServant
            };
            createController = function() {
                $injector.get('$controller')("PriceInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:priceInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
