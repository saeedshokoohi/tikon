'use strict';

describe('Controller Tests', function() {

    describe('MetaTag Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMetaTag, MockCompany, MockServiceItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMetaTag = jasmine.createSpy('MockMetaTag');
            MockCompany = jasmine.createSpy('MockCompany');
            MockServiceItem = jasmine.createSpy('MockServiceItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'MetaTag': MockMetaTag,
                'Company': MockCompany,
                'ServiceItem': MockServiceItem
            };
            createController = function() {
                $injector.get('$controller')("MetaTagDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:metaTagUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
