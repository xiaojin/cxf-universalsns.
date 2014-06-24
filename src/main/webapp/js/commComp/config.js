var conf = {};
(function($){
	var ec2 = true;  
	if (ec2) {
	    conf.HOSTURL = "https://54.187.87.218:8443";
	    $.extend(conf,{
	        TWITTER_TOKENSERVICE:conf.HOSTURL+"/cxf/token/twitterRequest",
            LINKEDIN_TOKENSERVICE:conf.HOSTURL+"/cxf/token/linkedinRequest",
            GOOGLE_TOKENSERVICE:conf.HOSTURL+"/cxf/token/googleRequest",
            FACEBOOK_TOKENSERVICE:conf.HOSTURL+"/cxf/token/facebookRequest",
            GOOGLE_REFRESHTOKENSERVICE:conf.HOSTURL+"/cxf/token/googleRefreshToken"
	    });
	} else {
	    conf.HOSTURL="https://127.0.0.1:8443";
	    $.extend(conf,{
	        TWITTER_TOKENSERVICE:conf.HOSTURL+"/cxf/token/twitterRequest",
            LINKEDIN_TOKENSERVICE:conf.HOSTURL+"/cxf/token/linkedinRequest",
            GOOGLE_TOKENSERVICE:conf.HOSTURL+"/cxf/token/googleRequest",
            FACEBOOK_TOKENSERVICE:conf.HOSTURL+"/cxf/token/facebookRequest",
            GOOGLE_REFRESHTOKENSERVICE:conf.HOSTURL+"/cxf/token/googleRefreshToken"
	    });
	   
	}	
	
})(jQuery);

