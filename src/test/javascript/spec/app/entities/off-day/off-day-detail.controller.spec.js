'use strict';

describe('Controller Tests', function() {

    describe('OffDay Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOffDay, MockDatePeriod;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOffDay = jasmine.createSpy('MockOffDay');
            MockDatePeriod = jasmine.createSpy('MockDatePeriod');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'OffDay': MockOffDay,
                'DatePeriod': MockDatePeriod
            };
            createController = function() {
                $injector.get('$controller')("OffDayDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:offDayUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
