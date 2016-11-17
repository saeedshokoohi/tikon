'use strict';

describe('Controller Tests', function() {

    describe('DiscountInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDiscountInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDiscountInfo = jasmine.createSpy('MockDiscountInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'DiscountInfo': MockDiscountInfo
            };
            createController = function() {
                $injector.get('$controller')("DiscountInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:discountInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
