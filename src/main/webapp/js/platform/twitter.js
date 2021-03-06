var hasToken = false;
var twitter = {
    //count=1&start=5
    listFavList : function() {
        var oauth_information = JSON.parse(localStorage.getItem("tokens-twitter"));
        if (oauth_information != null && oauth_information != undefined) {
            var api_key = "123456";
            function success(data) {
                social.tool.loading.hide();
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
                        var results = JSON.parse(data);
                        if (results.length > 0) {
                            var temple = Handlebars.compile($("#twitter-list-template").html());
                            $("#favList").html("");

                            Handlebars.registerHelper('userName', function() {
                                return this.user.name;
                            });

                            Handlebars.registerHelper('userID', function() {
                                return this.user.id;
                            });
                            Handlebars.registerHelper('userDesc', function() {
                                return this.user.description;
                            });

                            var context = {
                                "li_related" : results
                            };
                            $("#favList").append(temple(context));
                        }
                    }
                }
            }

            function error(jqXHR, textStatus, errorThrown) {
                social.tool.loading.hide();
                console.log(errorThrown);
            }

            function before(jqXHR) {
                social.tool.loading.show();
            }


            $.ajax({
                url : conf.HOSTURL+"/cxf/service/favlist/",
                type : "POST",
                success : success,
                contentType : "application/json",
                error : error,
                beforeSend : before,
                data : JSON.stringify({
                        ApiEntity : {
                            platform : "twitter",
                            accessToken : oauth_information.access_token,
                            accessTokenSec:oauth_information.access_token_sec,
                            apiKey : api_key,
                            twitterEntity : {
                                consumerKeySec : conf.TWITTER_SEC,
                                consumerKey : conf.TWITTER_KEY,
                                count : '4'
                            }
                        }
                    })
            });

        } else {
            alert("get token first");
        }

    },
    postStatuses : function() {
        var oauth_information = JSON.parse(localStorage.getItem("tokens-twitter"));
        if (oauth_information != null && oauth_information != undefined) {
            var status = $("#commandValue").val();
            var api_key = "123456";
            if (status == null || status == undefined) {
                alert("please say something");
            } else {
                status = escape(status).replace(/\@/g, '%40').replace(/\*/g, '%2A').replace(/\//g, '%2F').replace(/\+/g, '%2B');

                $.ajax({
                    url : conf.HOSTURL+"/cxf/service/message/",
                    type : "POST",
                    contentType : "application/json",
                    dataType : 'xml',
                    success : success,
                    error : error,
                    beforeSend : before,
                    data : JSON.stringify({
                        ApiEntity : {
                            platform : "twitter",
                            accessToken : oauth_information.access_token,
                            accessTokenSec:oauth_information.access_token_sec,
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
                social.tool.loading.hide();
                var id = $.xml2json(data).id;
                if (id == undefined) {
                    var error = $.xml2json(data);
                    alert(error.message);
                    window.location.href = conf.TWITTER_CALLBACK;
                    ;
                    localStorage.clear();
                    $("#twitter-token").html("");
                } else {
                        var result =  $.xml2json(data);
                        var mycomment = "<label class='alert alert-success comment-style'>" + status + "</label>";
                        $("#status").append(mycomment);
                        // alert(JSON.parse(data).created_at +':\n'+ JSON.parse(data).text);
                }
            }

            function error(jqXHR, textStatus, errorThrown) {
                social.tool.loading.hide();
                console.log(errorThrown);
            }

            function before(jqXHR) {
                social.tool.loading.show();
            }

        } else {
            alert("get token first");
        }

    },
    getUserProfile:function(){
       var oauth_information = JSON.parse(localStorage.getItem("tokens-twitter"));
        if (oauth_information != null && oauth_information != undefined) {
            var api_key = "123456";
            function success(data) {
                social.tool.loading.hide();
                console.log(data); 
                var id = $.xml2json(data).id;                              
                if (id == undefined) {
                    alert("error");
                    window.location.href = conf.TWITTER_CALLBACK;
                    localStorage.clear();
                    $("#twitter-token").html("");
                } else {
                    var user = $.xml2json(data);
                    $("#user-id").html(user.id);
                    $("#name").html(user.username);
                    $("#desc").html(user.description);
                }
            }

            function error(jqXHR, textStatus, errorThrown) {
                social.tool.loading.hide();
                console.log(errorThrown);
            }

            function before(jqXHR) {
                social.tool.loading.show();
            }


            $.ajax({
                url : conf.HOSTURL+"/cxf/service/profile/",
                type : "POST",
                success : success,
                contentType : "application/json",
                dataType : 'xml',
                error : error,
                beforeSend : before,
                data : JSON.stringify({
                        ApiEntity : {
                            platform : "twitter",
                            accessToken : oauth_information.access_token,
                            accessTokenSec:oauth_information.access_token_sec,
                            apiKey : api_key,
                            twitterEntity : {
                                consumerKeySec : conf.TWITTER_SEC,
                                consumerKey : conf.TWITTER_KEY,
                            }
                        }
                    })
            });

        } else {
            alert("get token first");
        }
        
    },
    
    oauthTwitter : function() {
        var twitterToken = JSON.parse(localStorage.getItem("tokens-twitter"));
        if (twitterToken != null && twitterToken.access_token != undefined) {
            $("#twitter-token").html(twitterToken.access_token);
            alert("Got one");
        } else {          
            var udid = "be34333bae754655a5f808420af68316";
            udid =localStorage.getItem("login-udid");
            if(udid != undefined){
                 var sns = SNS(udid);
                 sns.twitter_token(function(){
                    var twitterToken = JSON.parse(localStorage.getItem("tokens-twitter"));
                    $("#twitter-token").html(twitterToken.access_token);
                 });
            }else{
                alert("Sigin first");
            }

        }

    }, 
    
    // authRequest(conf.TWITTER);
      // var ss = localStorage.getItem("tokens-facebook")
    ready : function() {
        $("#twitter-get-token").click(twitter.oauthTwitter);
        $("#twitter-get-profile").click(twitter.getUserProfile);
        $("#twitterFav").click(twitter.listFavList);
        $("#sharecommand").click(twitter.postStatuses);
        // oauthV1.oauthv1Search();
    
       
        var twitterToken = JSON.parse(localStorage.getItem("tokens-twitter"));
        if (twitterToken != null && twitterToken.access_token != undefined) {
            $("#twitter-token").html(twitterToken.access_token);
        }

    }
};

$(twitter.ready);
