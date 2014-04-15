var hasToken = false;
var twitter = {
    //count=1&start=5
    listFavList : function() {
        var oauth_information = JSON.parse(localStorage.getItem("twitteroauth"));
        if (oauth_information != null && oauth_information != undefined) {
            oauthV1.init();
            var sourceUrl = "https://api.twitter.com/1.1/favorites/list.json";
            var parameters = "count=1";
            var action = "Get";
            var oauth_information = JSON.parse(localStorage.getItem("twitteroauth"));
            var url = oauth.sign({
                action : action,
                path : sourceUrl,
                parameters : parameters,
                signatures : oauth_information
            }).signed_url;
            console.log(url);
            function success(data) {
                console.log(data);
                if (JSON.parse(data).errors != undefined) {
                    alert(JSON.parse(data).errors[0].message);
                    window.location.href = conf.TWITTER_CALLBACK;
                    localStorage.clear();
                    $("#twitter-token").html("");
                } else {
                    if (JSON.parse(data).length == 0) {
                        alert("You haven't add any favourite list yet");
                    } else {
                        alert(JSON.parse(data)[0].user.description);
                    }
                }
            }

            function error(jqXHR, textStatus, errorThrown) {
                console.log(errorThrown);
            }


            $.ajax({
                url : "/cxf/service/favlist/",
                type : "POST",
                success : success,
                contentType : "application/json",
                error : error,
                data : JSON.stringify({
                    ApiEntity : {
                        tokenURL : escape(url),
                        platform : "twitter",                   
                    }
                })
            }); 

        } else {
            alert("get token first");
        }

    },
    postStatuses :function(){
        var oauth_information = JSON.parse(localStorage.getItem("twitteroauth"));
        if (oauth_information != null && oauth_information != undefined) {
            oauthV1.init();
            var sourceUrl = "https://api.twitter.com/1.1/statuses/update.json";
            var status = $("#commandValue").val();
            var api_key = "123456";
            if (status == null || status == undefined) {
                alert("please say something");
            } else {
                status = escape(status).replace(/\@/g, '%40').replace(/\*/g, '%2A').replace(/\//g, '%2F').replace(/\+/g, '%2B');
     
  
                $.ajax({
                    url : "/cxf/service/message/",
                    type : "POST",
                    contentType : "application/json",
                    success : success,
                    error : error,
                    data : JSON.stringify({
                        ApiEntity : {
                            platform : "twitter",
                            accessToken : JSON.stringify(oauth_information),
                            apiKey : api_key,
                            twitterEntity : {
                                consumerKeySec : conf.TWITTER_SEC,
                                consumerKey : conf.TWITTER_KEY,
                                status : status
                            }
                        }
                    })
                });


            }
            function success(data) {
                if (JSON.parse(data).errors != undefined) {
                    alert(JSON.parse(data).errors[0].message);
                    window.location.href = conf.TWITTER_CALLBACK;;
                    localStorage.clear();
                    $("#twitter-token").html("");
                } else {
                    if (JSON.parse(data).length == 0) {
                        alert("You failed to post yet");
                    } else {
                        alert(JSON.parse(data).created_at +':\n'+ JSON.parse(data).text);
                    }
                }
            }
            function error(jqXHR, textStatus, errorThrown) {
                console.log(errorThrown);
            }

        }else
        {
            alert("get token first");
        }

    },
    oauthTwitter : function() {
        var twitterToken = JSON.parse(localStorage.getItem("twitteroauth"));
        if(twitterToken!=null && twitterToken.oauth_token!=undefined){
             $("#twitter-token").html(twitterToken.oauth_token);
             alert("Got one");
        }else
        {
             authRequest(conf.TWITTER);
        }
       
    },
    ready : function() {  
        $("#twitter-get-token").click(twitter.oauthTwitter);
        $("#twitterFav").click(twitter.listFavList);       
        $("#sharecommand").click(twitter.postStatuses); 
        oauthV1.oauthv1Search();

        var twitterToken = JSON.parse(localStorage.getItem("twitteroauth"));
        if (twitterToken != null && twitterToken.oauth_token != undefined) {
            $("#twitter-token").html(twitterToken.oauth_token);
        }

    }
};

$(twitter.ready);
