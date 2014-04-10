var shared_secret, consumer_key, proxy, callbackUrl, request_tokenURL, auth_tokenURL, access_tokenURL, oauth = null;
var oauth_information = {};
var authRequest = null;
if ( typeof social === "undefined" || !social) {
    var social = {};
}
social.tool = {
    queryString : function(query) {
        // This function is anonymous, is executed immediately and
        // the return value is assigned to QueryString!
        var query_string = {};
        var vars = query.split("&");
        for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split("=");
            // If first entry with this name
            if ( typeof query_string[pair[0]] === "undefined") {
                query_string[pair[0]] = pair[1];
                // If second entry with this name
            } else if ( typeof query_string[pair[0]] === "string") {
                var arr = [query_string[pair[0]], pair[1]];
                query_string[pair[0]] = arr;
                // If third or later entry with this name
            } else {
                query_string[pair[0]].push(pair[1]);
            }
        }
        return query_string;
    },

    invokeWebServiceCall : function(opt) {
        opt = $.extend({
            url : '',
            data : '',
            beforeSend : function() {
            },
            success : function(data) {
            },
            error : function(jqXHR, textStatus, errorThrown) {
            },
            complete : function() {
            }
        }, opt || {});

        $.ajax({
            url : opt.url,
            type : opt.type,
            contentType : 'text/plain',
            beforeSend : opt.beforeSend,
            success : opt.success,
            error : opt.error,
            complete : opt.complete,
            xhrFields : {
                // The 'xhrFields' property sets additional fields on the XMLHttpRequest.
                // This can be used to set the 'withCredentials' property.
                // Set the value to 'true' if you'd like to pass cookies to the server.
                // If this is enabled, your server must respond with the header
                // 'Access-Control-Allow-Credentials: true'.
                withCredentials : false
            },
            headers : {
                // Set any custom headers here.
                // If you set any non-simple headers, your server must include these
                // headers in the 'Access-Control-Allow-Headers' response header.
            },
        });
    }
};



authRequest = function(platform) {
    
    if (platform === conf.TWITTER) {
        localStorage.setItem("webSite", conf.TWITTER);
        oauthV1.init();
        oauthV1.start();
    }
    if (platform === conf.LINKEDIN) {
        localStorage.setItem("webSite", conf.LINKEDIN);
        var lineninToken = JSON.parse(localStorage.getItem("takends-linkedin"));
        if(lineninToken!=null && lineninToken.access_token!=undefined){
            link.listLinkedPeople();
        }else{
            var c = Math.round(new Date().getTime() / 1000);
            oauthV2.init();
            jso_ensureTokens({
                "linkedin" : ["r_basicprofile","rw_nus","rw_company_admin"]
            });
            jso_dump();
        }

    }
    if (platform === conf.FACEBOOK) {
        localStorage.setItem("webSite", conf.FACEBOOK);
        oauthV2.init();
        // jso_wipe();
        jso_ensureTokens({
            "facebook" : ["read_stream"]
        });
        jso_dump();
    }
    if (platform === conf.GOOGLEPLUS) {
        localStorage.setItem("webSite", conf.GOOGLEPLUS);
        oauthV2.init();
        jso_wipe();
        jso_ensureTokens({
            "google" : ["https://www.googleapis.com/auth/plus.me"],
        });
        jso_dump();
    };
};

var oauthV1 = {
    init : function() {
        if (localStorage.getItem("webSite") == "LinkedIn") {
            shared_secret = conf.LINKEDIN_SEC;
            consumer_key = conf.LINKEDIN_KEY;
            proxy = conf.PROXY;
            callbackUrl = conf.LINKEDIN_CALLBACK;
            request_tokenURL = conf.LINKEDIN_REQUESTTOKEN_URL;
            auth_tokenURL = conf.LINKEDIN_AUTHTOKEN_URL;
            access_tokenURL = conf.LINKEDIN_ACCESSTOKEN_URL;
            oauth = OAuthSimple(consumer_key, shared_secret);
        }
        if (localStorage.getItem("webSite") == "Twitter") {
            shared_secret = conf.TWITTER_SEC;
            consumer_key = conf.TWITTER_KEY;
            proxy = conf.PROXY;
            callbackUrl = conf.TWITTER_CALLBACK;
            request_tokenURL = conf.TWITTER_REQUESTTOKEN_URL;
            auth_tokenURL = conf.TWITTER_AUTHTOKEN_URL;
            access_tokenURL = conf.TWITTER_ACCESSTOKEN_URL;
            oauth = OAuthSimple(consumer_key, shared_secret);
        }
    },

    start : function() {
        oauth.reset();
        var url = oauth.sign({
            action : "GET",
            path : request_tokenURL,
            parameters : {
                oauth_callback : callbackUrl
            }
        }).signed_url;
        console.log(url);
        function success(data) {
            console.log(data);
            localStorage.setItem("1", "1");
            data.replace(new RegExp("([^?=&]+)(=([^&]*))?", "g"), function($0, $1, $2, $3) {
                oauth_information[$1] = $3;
            });
            window.location.href = auth_tokenURL + oauth_information.oauth_token;
            
            localStorage.setItem("authInfo", JSON.stringify(oauth_information));
            var interval = setInterval(function() {
                if (localStorage.getItem(oauth_information.oauth_token) != null) {
                    clearInterval(interval);
                    oauthV1.getAccessToken(oauth_information, localStorage.getItem(oauth_information.oauth_token));
                }
            }, 500);
        }

        function error(jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
        }


        social.tool.invokeWebServiceCall({
            type : 'GET',
            url : proxy + "?url=" + escape(url),
            success : success,
            error : error
        });
    },
    getAccessToken : function(oauth_information, verifier) {
        oauth.reset();
        var url = oauth.sign({
            action : "GET",
            path : access_tokenURL,
            parameters : {
                oauth_verifier : verifier
            },
            signatures : oauth_information
        }).signed_url;
        console.log(url);
        function success(data) {
            console.log(data);
            data.replace(new RegExp("([^?=&]+)(=([^&]*))?", "g"), function($0, $1, $2, $3) {
                oauth_information[$1] = $3;
            });
            
               if (localStorage.getItem("webSite") == conf.TWITTER) {
                   localStorage.setItem("twitteroauth", JSON.stringify(oauth_information));
                   // twitter.listFavList(oauth_information);
               }
        }

        function error(jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
        }


        social.tool.invokeWebServiceCall({
            type : 'GET',
            url : proxy + "?url=" + escape(url),
            success : success,
            error : error
        });
    },

    oauthv1Search : function() {
        if (localStorage.getItem("webSite") === conf.TWITTER) {
            var twitterToken = JSON.parse(localStorage.getItem("twitteroauth"));
            if (twitterToken != null && twitterToken.oauth_token != undefined) {

            } else {
                var search = window.location.search;
                var query = "";
                if (search.length > 0) {
                    query = search.substring(1);
                    var q = social.tool.queryString(query);
                    localStorage.setItem(q.oauth_token, q.oauth_verifier);
                    oauth_information = JSON.parse(localStorage.getItem("authInfo"));
                    oauthV1.init();
                    oauthV1.getAccessToken(oauth_information, localStorage.getItem(oauth_information.oauth_token));
                }
            }
        }else if(localStorage.getItem("webSite") === conf.LINKEDIN){
            var lineninToken = JSON.parse(localStorage.getItem("takends-linkedin"));
            if (lineninToken != null && lineninToken.access_token != undefined) {
                // link.listLinkedPeople();
            } else {
                var search = window.location.search;
                var query = "";
                if (search.length > 0) {
                    query = search.substring(1);
                    var q = social.tool.queryString(query);
                    oauthV2.linkedGetAccessToken(q.code, q.state);
                    // window.close();
                }
            }
        }
    }

};


var oauthV2 = {
    init : function() {
        jso_configure({
            "google" : {
                client_id : "813483280675-h879tteoivpnmfeujgf2lg1j9t2jk9n7.apps.googleusercontent.com",
                redirect_uri : "http://localhost:9090/google.html",
                authorization : "https://accounts.google.com/o/oauth2/auth",
                isDefault : true
            },
            "facebook" : {
				client_id : "1455916827976099",
				redirect_uri : "http://127.0.0.1:9090/facebook.html",
				authorization : "https://www.facebook.com/dialog/oauth",
				presenttoken : "qs"
			},
			"linkedin":{
			    response_type:'code',
			    client_id:'77b11et5lruzhu',
			    state:'DWQSCA26832plwucs409',
			    redirect_uri:'http://127.0.0.1:9090/linkedin.html',
			    authorization : "https://www.linkedin.com/uas/oauth2/authorization",
			}
        });
    },
    linkedGetAccessToken:function(code,state){
        var parameter = {
            grant_type : 'authorization_code',
            code : code,
            redirect_uri : 'http://127.0.0.1:9090/linkedin.html',
            client_id : '77b11et5lruzhu',
            client_secret : 'wv4Y4t2rJ7sSljsV'
        }; 

        function success(data) {
            var responseString = JSON.parse(data);
            if (responseString!=null&&responseString.access_token!=undefined) {
                     localStorage.setItem("takends-linkedin", data);
                     $("#linkedin-token").text(responseString.access_token);
                     // overall.listLinkedPeople();
            }
            else
            {
                     console.log(data);
                     alert(data);
            }
        }

        function error(jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
        }

        function EncodeQueryData(data) {
            var ret = [];
            for (var d in data)
            ret.push(d + "=" + data[d]);
            return ret.join("&");
        }
        var url = "https://www.linkedin.com/uas/oauth2/accessToken"+'?'+EncodeQueryData(parameter);
        social.tool.invokeWebServiceCall({
            type : 'POST',
            url:conf.PROXY + "?url=" +escape(url),
            success : success,
            error : error
        }); 
    }
    
};


