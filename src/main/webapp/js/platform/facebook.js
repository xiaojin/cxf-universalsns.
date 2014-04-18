var app = {
    ready : function() {
        app.bind();
        //push accesstoken from URL parameter to localstorage
        oauthV2.oauthSearch();
        
        var obj = JSON.parse(localStorage.getItem("tokens-facebook"));
        if (obj != undefined) {
            var local_token = obj[0].access_token;
            $("#facebook-token").text(local_token);
        }
    },
    initToken : function() {
        var obj = JSON.parse(localStorage.getItem("tokens-facebook"));
        if (obj != undefined) {
            var local_token = obj[0].access_token;
            $("#facebook-token").text(local_token);
        } else {
            social.tool.loading.show();
            authRequest(conf.FACEBOOK);
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
        $("#googleURL").click(function() {
            window.location.href = conf.GOOGLE_CALLBACK;
        });
    },

    post : function() {
        var api_key = "123456";
        var param = $("#parameter").val();
        var obj = JSON.parse(localStorage.getItem("tokens-facebook"));
        var local_token = obj[0].access_token;

        function success(data) {
            social.tool.loading.hide();
            var status = data.id;
            if (status != null && status != undefined) {
                var mycomment = "<label class='alert alert-success comment-style'>" + param + "</label>";
                $("#status").append(mycomment);
            } else {
                console.log(data);
                alert("You failed to post yet");
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
            url : "/cxf/service/message/",
            type : "POST",
            success : success,
            error : error,
            beforeSend : before,
            dataType : 'json',
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
        var local_token = obj[0].access_token;

        function success(data) {
            var userid = data.id;
            social.tool.loading.hide();
            if (userid != null && userid != undefined) {
                $("#user-id").html(data.id);
                $("#name").html(data.name);
                $("#gender").html(data.gender);
                $("#link").html(data.link);
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
            url : "/cxf/service/profile/",
            type : "POST",
            success : success,
            dataType : 'json',
            contentType : "application/json",
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
