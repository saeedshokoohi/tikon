(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('SelectorDataQuery', SelectorDataQuery);

    SelectorDataQuery.$inject = ['$resource'];

    function SelectorDataQuery ($resource) {

        var findByType=function(type)
        {
          var resourceUrl='api/selector-data-by-key/'+type;
            return $resource(resourceUrl, {}, {
                'query': { method: 'GET', isArray: true}
                }

            );
        };
        var findByTypeAndParent=function(type,parent)
        {
            if(parent==null)parent=0;
            var resourceUrl='api/selector-data-by-key/'+type+'/'+parent;
            return $resource(resourceUrl, {}, {
                    'query': { method: 'GET', isArray: true}
                }

            );
        };


        return {
            findByType:findByType,
            findByTypeAndParent:findByTypeAndParent
        };
    }
})();
