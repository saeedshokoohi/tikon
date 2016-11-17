'use strict';

describe('Controller Tests', function() {

    describe('ServiceCapacityInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockServiceCapacityInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockServiceCapacityInfo = jasmine.createSpy('MockServiceCapacityInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ServiceCapacityInfo': MockServiceCapacityInfo
            };
            createController = function() {
                $injector.get('$controller')("ServiceCapacityInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:serviceCapacityInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
