'use strict';

describe('Controller Tests', function() {

    describe('OrderBagItemOption Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOrderBagItemOption, MockOrderBagServiceItemDtail, MockServiceOptionItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOrderBagItemOption = jasmine.createSpy('MockOrderBagItemOption');
            MockOrderBagServiceItemDtail = jasmine.createSpy('MockOrderBagServiceItemDtail');
            MockServiceOptionItem = jasmine.createSpy('MockServiceOptionItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'OrderBagItemOption': MockOrderBagItemOption,
                'OrderBagServiceItemDtail': MockOrderBagServiceItemDtail,
                'ServiceOptionItem': MockServiceOptionItem
            };
            createController = function() {
                $injector.get('$controller')("OrderBagItemOptionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:orderBagItemOptionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
