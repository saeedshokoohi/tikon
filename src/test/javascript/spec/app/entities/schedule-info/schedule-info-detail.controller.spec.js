'use strict';

describe('Controller Tests', function() {

    describe('ScheduleInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockScheduleInfo, MockWeeklyScheduleInfo, MockServiceItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockScheduleInfo = jasmine.createSpy('MockScheduleInfo');
            MockWeeklyScheduleInfo = jasmine.createSpy('MockWeeklyScheduleInfo');
            MockServiceItem = jasmine.createSpy('MockServiceItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ScheduleInfo': MockScheduleInfo,
                'WeeklyScheduleInfo': MockWeeklyScheduleInfo,
                'ServiceItem': MockServiceItem
            };
            createController = function() {
                $injector.get('$controller')("ScheduleInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:scheduleInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
