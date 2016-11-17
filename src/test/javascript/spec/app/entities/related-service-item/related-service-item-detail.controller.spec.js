'use strict';

describe('Controller Tests', function() {

    describe('RelatedServiceItem Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRelatedServiceItem, MockServiceItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRelatedServiceItem = jasmine.createSpy('MockRelatedServiceItem');
            MockServiceItem = jasmine.createSpy('MockServiceItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'RelatedServiceItem': MockRelatedServiceItem,
                'ServiceItem': MockServiceItem
            };
            createController = function() {
                $injector.get('$controller')("RelatedServiceItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:relatedServiceItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
