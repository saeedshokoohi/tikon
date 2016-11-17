angular.module('tikonApp').directive('eoTimePicker', function () {
        return {
            restrict: 'E',
         //   templateUrl: 'app/components/eyeson/time-picker/eyeson.time-picker.template.html',

            //   template: '<input class="datePickerClass" />',
            scope: {
                timeValue: '=', //use =? for optionality,
                id: '@'

            },
            link: function (scope, element, attributes) {
             //  debugger;
                var defaultTime='00 : 00';
                var timeValueStr =defaultTime ;

                var hasInited = false;
                var el = $(element);
                var onChange = function (str) {
debugger;
                    if (hasInited && str!=defaultTime) {
                    //    debugger;
                        var hour = str.split(":")[0].trim();
                        var min = str.split(":")[1].trim();
                        scope.timeValue = new Date(1970, 0, 1, hour, min, 0);
                        scope.$apply();
                    }
                }
                var makeTimePicker = function (time) {
                 //   debugger;
                    $(el).empty();
                    var div=$('<div class=" input-group date-picker"></div>');
                    div.append('<input class="form-control text-center timepicker"  type="text" style="direction: ltr"  />');
                    $(el).append(div);
                    var options = {
                        now: time, //hh:mm 24 hour format only, defaults to current time
                        twentyFour: true,  //Display 24 hour format, defaults to false
                        upArrow: 'wickedpicker__controls__control-up',  //The up arrow class selector to use, for custom CSS
                        downArrow: 'wickedpicker__controls__control-down', //The down arrow class selector to use, for custom CSS
                        close: 'wickedpicker__close', //The close class selector to use, for custom CSS
                        hoverState: 'hover-state', //The hover state class to use, for custom CSS
                        title: 'انتخاب ساعت', //The Wickedpicker's title,
                        showSeconds: false, //Whether or not to show seconds,
                        secondsInterval: 1, //Change interval for seconds, defaults to 1,
                        minutesInterval: 1, //Change interval for minutes, defaults to 1
                        beforeShow: null, //A function to be called before the Wickedpicker is shown
                        show: null, //A function to be called when the Wickedpicker is shown
                        clearable: true, //Make the picker's input clearable (has clickable "x")
                        valueChanged: onChange,
                    };

                    //var div = $('<div ></div>').addClass('input-group date-picker');
                    //var input = $('<input  id="' + $attrs.id + '" class="form-control text-center timepicker"  type="text" style="direction: ltr"  ng-model="$ctrl.timeValueStr"   />');
                    //
                    //div.append(input);
                    //input.wickedpicker(options);
                    //$($element).append(div);
                      $('.timepicker', el).wickedpicker(options,0);
                }

                var setTime=function(timeValue)
                {
                    if (timeValue) {
                        console.log(timeValue);
                        var temp = new Date(timeValue);
                        if (temp != 'Invalid Date') {
                            //on post link
                            //     alert('in'+temp);
                    //        debugger;


                            timeValueStr = temp.getHours() + ":" + temp.getMinutes();
                        }
                        else {
                            timeValueStr =defaultTime;
                        }
                        hasInited=false;
                        makeTimePicker(timeValueStr);
                        hasInited=true;


                    }
                }
         //       makeTimePicker(timeValueStr);
             //   if(scope.timeValue==undefined)
                console.log('inited...');
                setTime(defaultTime);

                hasInited=true;

                //the $watch method works when the model values have not yet been processed by the time this directive runs
                scope.$watch('timeValue', function (timeValue) {
                 setTime(timeValue);
                });

                //$observe is useless here, as there is no interpolation whatsoever, the $watch will react to changes from both parent scope and current scope, furthermore any change to the value here, changes the parent value as well

                //=? is required if you want to assign values to the current isolated scope property, but the DOM attribute or parent scope property was never defined, checking if it exists or logging its current value won't result in an error

            }
        };
    }
);
