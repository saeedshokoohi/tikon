'use strict';

describe('Controller Tests', function() {

    describe('NotificationSetting Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockNotificationSetting;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockNotificationSetting = jasmine.createSpy('MockNotificationSetting');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'NotificationSetting': MockNotificationSetting
            };
            createController = function() {
                $injector.get('$controller')("NotificationSettingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:notificationSettingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
