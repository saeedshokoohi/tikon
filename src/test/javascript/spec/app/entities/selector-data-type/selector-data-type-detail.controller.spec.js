'use strict';

describe('Controller Tests', function() {

    describe('SelectorDataType Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSelectorDataType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSelectorDataType = jasmine.createSpy('MockSelectorDataType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SelectorDataType': MockSelectorDataType
            };
            createController = function() {
                $injector.get('$controller')("SelectorDataTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:selectorDataTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
