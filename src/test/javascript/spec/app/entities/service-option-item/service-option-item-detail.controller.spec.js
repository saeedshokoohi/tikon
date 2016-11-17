'use strict';

describe('Controller Tests', function() {

    describe('ServiceOptionItem Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockServiceOptionItem, MockServiceOptionInfo, MockPriceInfo, MockAlbumInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockServiceOptionItem = jasmine.createSpy('MockServiceOptionItem');
            MockServiceOptionInfo = jasmine.createSpy('MockServiceOptionInfo');
            MockPriceInfo = jasmine.createSpy('MockPriceInfo');
            MockAlbumInfo = jasmine.createSpy('MockAlbumInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ServiceOptionItem': MockServiceOptionItem,
                'ServiceOptionInfo': MockServiceOptionInfo,
                'PriceInfo': MockPriceInfo,
                'AlbumInfo': MockAlbumInfo
            };
            createController = function() {
                $injector.get('$controller')("ServiceOptionItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:serviceOptionItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
