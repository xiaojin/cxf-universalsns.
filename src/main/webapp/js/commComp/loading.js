var social = {};

(function($){
social.tool = {
        /*Utility for page loading*/
    loading : {
        /*Show the page loading mask*/
        show : function(opt) {
            'use strict';
            var defaultMsg = "Loading...";
            opt = $.extend({
                msg : defaultMsg
            }, opt || {});

            $('.ess-loading-mask, .ess-loading-indicator-wrapper').remove();
            var $wrapper = $("<div class='ess-loading-indicator-wrapper'></div><div class='ess-loading-mask'>"+opt.msg+"</div>");
            $wrapper.appendTo('body');
            social.tool.loading.position();
        },
        /*Hide the page loading mask*/
        hide : function() {
            'use strict';
            $(".ess-loading-mask, .ess-loading-indicator-wrapper").css('opacity', '0').css('width', '0px').css('height', '0px');
        },
        position : function() {
            'use strict';
            if($('.ess-loading-mask').width() > 0){/*$('.ess-loading-mask').width()  will return null if mask not exist*/
                $('.ess-loading-mask').css({
                    position : 'absolute',
                    left : ($(window).width() - $('.ess-loading-mask').outerWidth()) / 2,
                    top : ($(window).height() - $('.ess-loading-mask').outerHeight()) / 2
                });
                $('.ess-loading-indicator-wrapper').css({
                    width : $(document).width(),
                    height : $(document).height()
                });
            }
        }
    }
};
})(jQuery);
