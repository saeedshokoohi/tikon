'use strict';

describe('Controller Tests', function() {

    describe('OrderBag Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOrderBag;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOrderBag = jasmine.createSpy('MockOrderBag');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'OrderBag': MockOrderBag
            };
            createController = function() {
                $injector.get('$controller')("OrderBagDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:orderBagUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
