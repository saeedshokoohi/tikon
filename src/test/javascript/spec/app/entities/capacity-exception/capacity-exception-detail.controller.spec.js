'use strict';

describe('Controller Tests', function() {

    describe('CapacityException Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCapacityException, MockServiceCapacityInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCapacityException = jasmine.createSpy('MockCapacityException');
            MockServiceCapacityInfo = jasmine.createSpy('MockServiceCapacityInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CapacityException': MockCapacityException,
                'ServiceCapacityInfo': MockServiceCapacityInfo
            };
            createController = function() {
                $injector.get('$controller')("CapacityExceptionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:capacityExceptionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
