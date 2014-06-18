var conf = {};
(function($){
	var ec2 = false;  
	if (ec2) {
	    conf = {
	        HOSTURL:"http://54.187.87.218:9090",
	        TWITTER_TOKENSERVICE:"http://54.187.87.218:9090/cxf/token/twitterRequest",
            LINKEDIN_TOKENSERVICE:"http://54.187.87.218:9090/cxf/token/linkedinRequest",
            GOOGLE_TOKENSERVICE:"http://54.187.87.218:9090/cxf/token/googleRequest",
            FACEBOOK_TOKENSERVICE:"http://54.187.87.218:9090/cxf/token/facebookRequest",
            GOOGLE_REFRESHTOKENSERVICE:"http://54.187.87.218:9090/cxf/token/googleRefreshToken"
	    };
	} else {
	    conf = {
	        HOSTURL:"http://127.0.0.1:9090",
	        TWITTER_TOKENSERVICE:"http://127.0.0.1:9090/cxf/token/twitterRequest",
	        LINKEDIN_TOKENSERVICE:"http://127.0.0.1:9090/cxf/token/linkedinRequest",
	        GOOGLE_TOKENSERVICE:"http://127.0.0.1:9090/cxf/token/googleRequest",
	        FACEBOOK_TOKENSERVICE:"http://127.0.0.1:9090/cxf/token/facebookRequest",
	        GOOGLE_REFRESHTOKENSERVICE:"http://127.0.0.1:9090/cxf/token/googleRefreshToken"
	    };
	}	
	
})(jQuery);

