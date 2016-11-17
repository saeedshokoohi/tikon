'use strict';

describe('Controller Tests', function() {

    describe('AlbumInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAlbumInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAlbumInfo = jasmine.createSpy('MockAlbumInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'AlbumInfo': MockAlbumInfo
            };
            createController = function() {
                $injector.get('$controller')("AlbumInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:albumInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
