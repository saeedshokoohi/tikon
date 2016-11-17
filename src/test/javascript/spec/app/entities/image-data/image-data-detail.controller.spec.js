'use strict';

describe('Controller Tests', function() {

    describe('ImageData Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockImageData, MockAlbumInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockImageData = jasmine.createSpy('MockImageData');
            MockAlbumInfo = jasmine.createSpy('MockAlbumInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ImageData': MockImageData,
                'AlbumInfo': MockAlbumInfo
            };
            createController = function() {
                $injector.get('$controller')("ImageDataDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:imageDataUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
