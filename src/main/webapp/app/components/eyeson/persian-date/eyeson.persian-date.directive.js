


var PersianDateFilter= function($filter)
{
    return function(input)
    {

        if(input == null){ return ""; }



        var  date=new JDate(new Date(input));
        if(date!=undefined)
        return date.format('YYYY/MM/DD');
        else
        return input;

    };
}
var PersianTimeFilter= function($filter)
{
    return function(input)
    {

        if(input == null){ return ""; }



        var  date=new Date(input);
        if(date!=undefined)
            return date.getHours()+":"+date.getMinutes();
        else
            return input;

    };
}
angular.module('tikonApp').filter('toPersianDate', PersianDateFilter);
angular.module('tikonApp').filter('toPersianTime', PersianTimeFilter);
PersianDateFilter.$inject=['$filter'];
PersianTimeFilter.$inject=['$filter'];
