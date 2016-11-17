'use strict';

describe('Controller Tests', function() {

    describe('PriceInfoDtail Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPriceInfoDtail;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPriceInfoDtail = jasmine.createSpy('MockPriceInfoDtail');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PriceInfoDtail': MockPriceInfoDtail
            };
            createController = function() {
                $injector.get('$controller')("PriceInfoDtailDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:priceInfoDtailUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
