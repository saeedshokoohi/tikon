'use strict';

describe('Controller Tests', function() {

    describe('SettingInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSettingInfo, MockThemeSettingInfo, MockNotificationSetting, MockFinancialSetting;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSettingInfo = jasmine.createSpy('MockSettingInfo');
            MockThemeSettingInfo = jasmine.createSpy('MockThemeSettingInfo');
            MockNotificationSetting = jasmine.createSpy('MockNotificationSetting');
            MockFinancialSetting = jasmine.createSpy('MockFinancialSetting');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SettingInfo': MockSettingInfo,
                'ThemeSettingInfo': MockThemeSettingInfo,
                'NotificationSetting': MockNotificationSetting,
                'FinancialSetting': MockFinancialSetting
            };
            createController = function() {
                $injector.get('$controller')("SettingInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:settingInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
