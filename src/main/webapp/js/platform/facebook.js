var app = {
    ready : function() {
        app.bind();
        //push accesstoken from URL parameter to localstorage
        // oauthV2.oauthSearch();
        
        var obj = JSON.parse(localStorage.getItem("tokens-facebook"));
        if (obj != undefined) {
            var local_token = obj.access_token;
            $("#facebook-token").text(local_token);
        }
    },
    initToken : function() {
        var obj = JSON.parse(localStorage.getItem("tokens-facebook"));
        if (obj != undefined) {
            var local_token = obj.access_token;
            $("#facebook-token").text(local_token);
        } else {
            var udid = "be34333bae754655a5f808420af68316";
            udid =localStorage.getItem("login-udid");
            if(udid != undefined){
                 var sns = SNS(udid);
                sns.facebook_token(function(){
            // var ss = localStorage.getItem("tokens-facebook");
                     var obj = JSON.parse(localStorage.getItem("tokens-facebook"));
                    $("#facebook-token").html(obj.access_token);
         });
        }else{
                alert("Sigin first");
            }
            // social.tool.loading.show();
            // authRequest(conf.FACEBOOK);
        }
    },

    bind : function() {
        $("#facebook-get-token").click(function() {
            app.initToken();
        });
        $("#facebook-post-status").click(function() {
            app.post();
        });
        $("#facebook-get").click(function() {
            app.get();
        });
    },

    post : function() {
        var api_key = "123456";
        var param = $("#parameter").val();
        var obj = JSON.parse(localStorage.getItem("tokens-facebook"));
        var local_token = obj.access_token;

        function success(data) {
            social.tool.loading.hide();
            var id = $.xml2json(data).id;
            if (id != undefined) {
                var mycomment = "<label class='alert alert-success comment-style'>" + param + "</label>";
                $("#status").append(mycomment);
            } else {
                console.log($.xml2json(data).toString);
                alert($.xml2json(data).message);
            }
        };
        function error(responseString) {
            social.tool.loading.hide();
            var str = responseString;
            console.log("Response (facebook):");
            console.log(str);
            alert("You failed to post yet");
        };
        function before(jqXHR) {
            social.tool.loading.show();
        }


        $.ajax({
            url : conf.HOSTURL+"/cxf/service/message/",
            type : "POST",
            success : success,
            error : error,
            beforeSend : before,
            dataType : 'xml',
            contentType : "application/json",
            data : JSON.stringify({
                ApiEntity : {
                    platform : 'facebook',
                    apiKey : api_key,
                    accessToken : local_token,
                    facebookEntity : {
                        personID : "me",
                        parameters : param
                    }
                }
            })
        });
    },
    get : function() {
        var api_key = "123456";
        var obj = JSON.parse(localStorage.getItem("tokens-facebook"));
        var local_token = obj.access_token;

        function success(data) {
            var user = $.xml2json(data);
            var userid = user.id;
            social.tool.loading.hide();
            if (userid != null && userid != undefined) {
                $("#user-id").html(user.id);
                $("#name").html(user.username);
                $("#gender").html(user.gender);
                $("#link").html(user.link);
            } else {
                alert("no id");
            }
        };

        function error(data) {
            social.tool.loading.hide();
            var str = data.responseText;
            console.log("Response (facebook):");
            console.log(str);
            alert("You failed to get profile");
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
                    platform : 'facebook',
                    apiKey : api_key,
                    accessToken : local_token,
                    facebookEntity : {
                        personID : "me"
                    }
                }
            })
        });
    }
};

$(app.ready);
