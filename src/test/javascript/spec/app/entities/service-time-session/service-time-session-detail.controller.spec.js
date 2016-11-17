'use strict';

describe('Controller Tests', function() {

    describe('ServiceTimeSession Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockServiceTimeSession;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockServiceTimeSession = jasmine.createSpy('MockServiceTimeSession');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ServiceTimeSession': MockServiceTimeSession
            };
            createController = function() {
                $injector.get('$controller')("ServiceTimeSessionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:serviceTimeSessionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
