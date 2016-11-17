'use strict';

describe('Controller Tests', function() {

    describe('SelectorData Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSelectorData, MockSelectorDataType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSelectorData = jasmine.createSpy('MockSelectorData');
            MockSelectorDataType = jasmine.createSpy('MockSelectorDataType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SelectorData': MockSelectorData,
                'SelectorDataType': MockSelectorDataType
            };
            createController = function() {
                $injector.get('$controller')("SelectorDataDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:selectorDataUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
