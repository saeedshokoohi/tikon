'use strict';

describe('Controller Tests', function() {

    describe('Servant Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockServant, MockPersonInfo, MockServiceCategory, MockServiceItem, MockPriceInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockServant = jasmine.createSpy('MockServant');
            MockPersonInfo = jasmine.createSpy('MockPersonInfo');
            MockServiceCategory = jasmine.createSpy('MockServiceCategory');
            MockServiceItem = jasmine.createSpy('MockServiceItem');
            MockPriceInfo = jasmine.createSpy('MockPriceInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Servant': MockServant,
                'PersonInfo': MockPersonInfo,
                'ServiceCategory': MockServiceCategory,
                'ServiceItem': MockServiceItem,
                'PriceInfo': MockPriceInfo
            };
            createController = function() {
                $injector.get('$controller')("ServantDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:servantUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
