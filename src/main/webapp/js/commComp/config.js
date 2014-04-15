var conf = {};
(function($){
	var ec2 = true;  
	$.ajax({
		"url":"EnvServlet",
		"async":false,
		"success":function(data){
			if(data.env == "dev"){
				ec2 = false;
			}
		}
	});
	if (ec2) {
	    conf = {
	        TWITTER_KEY : "2gi2C1blyjfks5R8pHViCQ",
	        TWITTER_SEC : "FaCqSiO7eGPIK9PUv9j6x5mCENWOt4WK4VMuYxQB8",
	        TWITTER_CALLBACK : "http://54.201.88.26:9090/twitter2.html",
	        TWITTER_REQUESTTOKEN_URL : "https://api.twitter.com/oauth/request_token",
	        TWITTER_AUTHTOKEN_URL : "https://api.twitter.com/oauth/authenticate?oauth_token=",
	        TWITTER_ACCESSTOKEN_URL : "https://api.twitter.com/oauth/access_token",
	        LINKEDIN_KEY : "77458v6dh4gny1",
	        LINKEDIN_SEC : "LrgI1ldDYHPuyMiX",
	        LINKEDIN_CALLBACK : "http://54.201.88.26:9090/linkedin.html",
	        LINKEDIN_REQUESTTOKEN_URL : "https://api.linkedin.com/uas/oauth/requestToken",
	        LINKEDIN_AUTHTOKEN_URL : "https://www.linkedin.com/uas/oauth/authenticate?oauth_token=",
	        LINKEDIN_ACCESSTOKEN_URL : "https://api.linkedin.com/uas/oauth/accessToken",
	        GOOGLE_CLIENTID : "317971618994-nuo529502295pkap9atvhhdkglia38vp.apps.googleusercontent.com",
	        GOOGLE_CALLBACK : "http://ec2-54-201-88-26.us-west-2.compute.amazonaws.com:9090/google.html",
	        FACEBOOK_CLIENTID : "481141291987497",
	        FACEBOOK_CALLBACK : "http://54.201.88.26:9090/facebook.html",
	        PROXY : "http://54.201.88.26:9090/AccessToken",
	        TWITTER : "Twitter",
	        FACEBOOK : 'Facebook',
	        GOOGLEPLUS : "GooglePlus",
	        LINKEDIN : "LinkedIn"
	    };
	} else {
	    conf = {
	        TWITTER_KEY : "qCEyFIf0RehbUglehaOug",
	        TWITTER_SEC : "dbcOAa4PSWn5bhmKLT1S5dslqIOAzTYpV145dj77h8",
	        TWITTER_CALLBACK : "http://127.0.0.1:9090/twitter2.html",
	        TWITTER_REQUESTTOKEN_URL : "https://api.twitter.com/oauth/request_token",
	        TWITTER_AUTHTOKEN_URL : "https://api.twitter.com/oauth/authenticate?oauth_token=",
	        TWITTER_ACCESSTOKEN_URL : "https://api.twitter.com/oauth/access_token",
	        LINKEDIN_KEY : "77b11et5lruzhu",
	        LINKEDIN_SEC : "wv4Y4t2rJ7sSljsV",
	        LINKEDIN_CALLBACK : "http://127.0.0.1:9090/linkedin.html",
	        LINKEDIN_REQUESTTOKEN_URL : "https://api.linkedin.com/uas/oauth/requestToken",
	        LINKEDIN_AUTHTOKEN_URL : "https://www.linkedin.com/uas/oauth/authenticate?oauth_token=",
	        LINKEDIN_ACCESSTOKEN_URL : "https://api.linkedin.com/uas/oauth/accessToken",
	        GOOGLE_CLIENTID : "829887619324-1ishqktkmpkk41nccbql4hiethu1re5f.apps.googleusercontent.com",
	        GOOGLE_CALLBACK : "http://127.0.0.1:9090/google.html",
	        FACEBOOK_CLIENTID : "1455916827976099",
	        FACEBOOK_CALLBACK : "http://127.0.0.1:9090/facebook.html",
	        PROXY : "http://localhost:9090/AccessToken",
	        TWITTER : "Twitter",
	        FACEBOOK : 'Facebook',
	        GOOGLEPLUS : "GooglePlus",
	        LINKEDIN : "LinkedIn"
	    };
	}	
	
})(jQuery);


