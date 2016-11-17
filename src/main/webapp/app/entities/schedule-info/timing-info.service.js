(function () {
    'use strict';
    angular
        .module('tikonApp')
        .factory('TimingInfo', TimingInfo);

    TimingInfo.$inject = ['$resource'];

    function TimingInfo($resource) {
        var resourceUrl = 'api/generateTimingInfo';

        var generateTimingInfo = $resource(resourceUrl, {}, {});

        return{
            generateTimingInfo:generateTimingInfo
        }
        ;
    }
})();
