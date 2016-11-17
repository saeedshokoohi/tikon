(function() {
    'use strict';
    function eoMultiselectController($scope,$attrs,$element) {
//debugger;
        var $ctrl = this;
        var blur = false;
        $ctrl.focused = false;
        $ctrl.testStr='It is test Str';

        //$ctrl.list = [];
        $ctrl.filteredItems = $ctrl.items;
        $ctrl.selPos = 0;
        $ctrl.elem=$element;

        $ctrl.focusIn = function () {
            //debugger;
            if (!$ctrl.focused) {
                $ctrl.focused = true;
                blur = false;
                $ctrl.selPos = 0;
            }
        };

        $ctrl.focusOut = function () {
            //debugger;
            $ctrl.itemsearch = "";
            if (!blur) {
                $ctrl.focused = false;
            } else {
                angular.element($ctrl.elem).find('input')[0].focus();
                blur = false;
            }
        };

        // Change me for custom display name on select list
        $ctrl.getDisplayItem = function (item) {
            //debugger;
            return item[$ctrl.displayitem];
        };

        // Change me for custom display name on tags (chips)
        $ctrl.getDisplayTag = function (item) {
            //debugger;
            return item[$ctrl.displaytag];
        };

        $ctrl.addItem = function (item) {
            //debugger;
            if (item) {
                $ctrl.list.push(item);
                $ctrl.itemsearch = "";
                blur = true;
                if ($ctrl.selPos >= $ctrl.filteredItems.length - 1) {
                    $ctrl.selPos--; // To keep hover selection
                }
             //   ngModel.$setViewValue($ctrl.list);
            }
         //   console.log($ctrl.list);
        };

        $ctrl.removeItem = function (item) {
            $ctrl.list.splice($ctrl.list.indexOf(item), 1);
       //     ngModel.$setViewValue($ctrl.list);
        };

        $ctrl.hover = function (index) {
            $ctrl.selPos = index;
        };

        $ctrl.keyPress = function (evt) {
            console.log(evt.keyCode);
            var keys = {
                38: 'up',
                40: 'down',
                8: 'backspace',
                13: 'enter',
                9: 'tab',
                27: 'esc'
            };

            switch (evt.keyCode) {
                case 27:
                    $ctrl.focusOut();
                    break;
                case 13:
                    if ($ctrl.selPos > -1) {
                        $ctrl.addItem($ctrl.filteredItems[$ctrl.selPos]);
                    }
                    break;
                case 8:
                    if (!$ctrl.itemsearch || $ctrl.itemsearch.length == 0) {
                        if ($ctrl.list.length > 0) {
                            $ctrl.list.pop();
                        }
                    }
                    break;
                case 38:
                    if ($ctrl.selPos > 0) {
                        $ctrl.selPos--;
                    }
                    break;
                case 40:
                    if ($ctrl.selPos < $ctrl.filteredItems.length - 1) {
                        $ctrl.selPos++;
                    }
                    break;
                default:
                    $ctrl.selPos = 0;
                    $ctrl.focusIn();
            }
        };


    }
    var eoMultiselect = {
       // template:'tets:{{$ctrl.testStr}}',
        templateUrl: 'app/components/eyeson/multi-select-autocomplete/eyeson.multi-select-autocomplete.template.html',
        controller:eoMultiselectController,
        bindings:
        {
            items :'<',
            displaytag:'@?',
            displayitem:'@?',
            list:'='

        }

    };

    angular
        .module('tikonApp')
        .component('eoMultiSelect', eoMultiselect)
        .directive('focus', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                attrs.$observe('focus', function (newValue) {
                    if (newValue == 'true') {
                        element[0].focus();
                    }
                });
            }
        }
    }).filter('notin', function () {
        return function (listin, listout) {
            return listin.filter(function (el) {
                return listout.indexOf(el) == -1;
            });
        };
    });

    eoMultiselectController.$inject = ['$scope','$attrs','$element'];


})();



/*
angular.module('tikonAppdirective',[])
        .directive('eo-multi-select', function () {
            return {
                restict: 'AEC',
                scope: {
                    items: '='
                },
                require: 'ngModel',
                link: function (scope, elem, attrs, ngModel) {
                    //debugger;
                    var blur = false;
                    scope.focused = false;
                    scope.list = [];
                    scope.filteredItems = scope.items;
                    scope.selPos = 0;

                    scope.focusIn = function () {
                        if (!scope.focused) {
                            scope.focused = true;
                            blur = false;
                            scope.selPos = 0;
                        }
                    };

                    scope.focusOut = function () {
                        scope.itemsearch = "";
                        if (!blur) {
                            scope.focused = false;
                        } else {
                            angular.element(elem).find('input')[0].focus();
                            blur = false;
                        }
                    };

                    // Change me for custom display name on select list
                    scope.getDisplayItem = function (item) {
                        return item[attrs.displayitem];
                    };

                    // Change me for custom display name on tags (chips)
                    scope.getDisplayTag = function (item) {
                        return item[attrs.displaytag];
                    };

                    scope.addItem = function (item) {
                        if (item) {
                            scope.list.push(item);
                            scope.itemsearch = "";
                            blur = true;
                            if (scope.selPos >= scope.filteredItems.length - 1) {
                                scope.selPos--; // To keep hover selection
                            }
                            ngModel.$setViewValue(scope.list);
                        }
                    };

                    scope.removeItem = function (item) {
                        scope.list.splice(scope.list.indexOf(item), 1);
                        ngModel.$setViewValue(scope.list);
                    };

                    scope.hover = function (index) {
                        scope.selPos = index;
                    };

                    scope.keyPress = function (evt) {
                        console.log(evt.keyCode);
                        var keys = {
                            38: 'up',
                            40: 'down',
                            8: 'backspace',
                            13: 'enter',
                            9: 'tab',
                            27: 'esc'
                        };

                        switch (evt.keyCode) {
                            case 27:
                                scope.focusOut();
                                break;
                            case 13:
                                if (scope.selPos > -1) {
                                    scope.addItem(scope.filteredItems[scope.selPos]);
                                }
                                break;
                            case 8:
                                if (!scope.itemsearch || scope.itemsearch.length == 0) {
                                    if (scope.list.length > 0) {
                                        scope.list.pop();
                                    }
                                }
                                break;
                            case 38:
                                if (scope.selPos > 0) {
                                    scope.selPos--;
                                }
                                break;
                            case 40:
                                if (scope.selPos < scope.filteredItems.length - 1) {
                                    scope.selPos++;
                                }
                                break;
                            default:
                                scope.selPos = 0;
                                scope.focusIn();
                        }
                    };
                },
                template: 'MULTI SELECT <div ng-cloak="" class="typeahead">\
        <ul data-ng-class="{\'focused\': focused}" \
            class="tags" data-ng-click="focusIn()">\
          <li class="tag" data-ng-repeat="s in list track by $index">\
            {{getDisplayTag(s)}} <span data-ng-click="removeItem(s)">x</span>\
          </li> \
          <li class="inputtag">\
            <input data-ng-blur="focusOut()" focus="{{focused}}" data-ng-model-options="{debounce: 500}" \
            type="text" data-ng-model="itemsearch" data-ng-keydown="keyPress($event)"/>\
          </li>\
        </ul>\
        <ul class="list" data-ng-show="focused">\
          <li data-ng-class="{\'active\': selPos == $index}" \
              data-ng-repeat="item in (filteredItems = (items | notin: list | filter: itemsearch | limitTo: 10)) track by $index" \
              data-ng-mousedown="addItem(item)" data-ng-mouseover="hover($index)">\
          {{getDisplayItem(item)}}\
          </li>\
        </ul>\
      </div>'
            };
        }).directive('focus', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                attrs.$observe('focus', function (newValue) {
                    if (newValue == 'true') {
                        element[0].focus();
                    }
                });
            }
        }
    }).filter('notin', function () {
        return function (listin, listout) {
            return listin.filter(function (el) {
                return listout.indexOf(el) == -1;
            });
        };
    });
*/



