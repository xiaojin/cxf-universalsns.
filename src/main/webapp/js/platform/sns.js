/**
 *    
 * Example:
       <code>
        var sns = SNS().facebook_token(callback);
        loca
       </code>
 * 
 * 
 * 
 * 
 * *
 */

var SNS ;
if(SNS === undefined)
{
    SNS = function(){
        var tokenURL = "";
        var tokenState = "";
        self.facebook_token = function(callback) {
            if (callback && typeof(callback) === 'function') {
                tokenURL = conf.FACEBOOK_TOKENSERVICE;
                tokenState = "tokens-facebook";
                snsrequest(tokenState, callback);
            }
        }; 


        self.twitter_token = function(callback) {
           if (callback && typeof(callback) === 'function') {
            tokenURL = conf.TWITTER_TOKENSERVICE;
            tokenState= "tokens-twitter";
            snsrequest(tokenState,callback);
            }
        };

        self.linkedin_token = function(callback) {
           if (callback && typeof(callback) === 'function') {
            tokenURL = conf.LINKEDIN_TOKENSERVICE;
            tokenState= "tokens-linkedin";
            snsrequest(tokenState,callback);
            }
        };

        self.google_token = function(callback) {
           if (callback && typeof(callback) === 'function') {
            tokenURL = conf.GOOGLE_TOKENSERVICE;
            tokenState= "tokens-google";
            snsrequest(tokenState,callback);
            }
        };

        self.snsrequest = function(state,callback) {
            var param = window.location.search;
            if (param.toLowerCase().indexOf("tokencallback") > -1) {
                handlerResponseToken(param,state,callback);
            } else {
                var myurl = window.document.URL;
                var width = 640;
                var height= 960;
                var left = (screen.width/2)-(width/2);
                var top = (screen.height/2)-(height/2);
                // var onwin = window.open(tokenURL+"?callback="+myurl,'','scrollbars=yes,width=' + width + ', height=' + height + ', top=' + top + ', left=' + left);
                var onwin = window.open(tokenURL,'','scrollbars=yes,width=' + width + ', height=' + height + ', top=' + top + ', left=' + left);
                var interval = setInterval(function() {
                    var param = onwin.location.search;
                    console.log(param);
                    if (param.toLowerCase().indexOf("tokencallback") > -1) {
                        if (param.indexOf("xml") >-1) {
                          handlerResponseToken(param,state,callback);
                          clearInterval(interval);                                                 
                        }
                        else
                        {
                            alert("Internal Error");
                        }
                        onwin.close();      
                    }
                }, 3000);
            }
        }; 
        
        
        self.handlerResponseToken = function(tokenParam,state,callback){
            console.log(tokenParam);
            tokenParam = decodeURI(tokenParam);
            var index = tokenParam.indexOf("=");
            tokenParam= tokenParam.substring(index + 1);
            
            var xmlDoc = $.parseXML(tokenParam),
            $xml = $(xmlDoc);
            var accessToken = $xml.find( "access_token" ).text()==null?"":$xml.find( "access_token" ).text();
            var tokenType = $xml.find( "token_type" ).text()==null?"":$xml.find( "token_type" ).text();
            var expires = $xml.find( "expires_in" ).text()==null?"":$xml.find( "expires_in" ).text();
            var userID = $xml.find( "user_id" ).text()==null?"":$xml.find( "user_id" ).text();
            var screenName = $xml.find( "screen_name" ).text()==null?"":$xml.find( "screen_name" ).text();
            var access_token_sec = $xml.find( "access_token_sec" ).text()==null?"":$xml.find( "access_token_sec" ).text();
            var refresh_token = $xml.find( "refresh_token" ).text()==null?"":$xml.find( "refresh_token" ).text();
            var authinfo ={
                "access_token":accessToken,
                "token_type":tokenType,
                "expires_in":expires,
                "user_id":userID,
                "screen_name":screenName,
                "access_token_sec":access_token_sec,
                "refresh_token":refresh_token
            };
            localStorage.setItem(state, JSON.stringify(authinfo));
            callback.call();
        };
        return self;
    };
}


