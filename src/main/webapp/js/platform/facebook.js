var app = {
    ready : function() {
        app.bind();
         authRequest(conf.FACEBOOK);
        // jso_configure({
            // "facebook" : {
                // client_id : conf.FACEBOOK_CLIENTID,
                // redirect_uri : conf.FACEBOOK_CALLBACK,
                // authorization : "https://www.facebook.com/dialog/oauth",
                // presenttoken : "qs"
            // }
        // });
        // jso_ensureTokens({
            // "facebook" : ["publish_actions", "read_stream"]
        // });
        // jso_dump();
    },
    getToken : function() {            
        var obj = JSON.parse(localStorage.getItem("tokens-facebook"));
        var local_token = obj[0].access_token;
        $("#facebook-token").text(local_token);
    },

    bind : function() {
        $("#facebook-get-token").click(function() {
            app.getToken();
        });
        $("#facebook-post-status").click(function() {
            app.post();
        });
        $("#facebook-get").click(function() {
            app.get();
        });

    },

    post : function() {
        var api_key = "key12345";
        var pa = $("#path").val();
        var param = $("#parameter").val();
        var obj = JSON.parse(localStorage.getItem("tokens-facebook"));
        var local_token = obj[0].access_token;

        function success(data) {
            var status = JSON.parse(data).id;
            if (status != null && status != undefined) {
                alert(param +"\n Post successfully");
            }
            else
            {
                console.log(data);
                alert(data);
            }
        };
        function error(responseString) {
            var str = responseString;
            console.log("Response (facebook):");
            console.log(str);
            alert(str);

        };
        $.ajax({
            url : "publishStatus",
            type : "POST",
            success : success,
            error : error,
            data : {
                "platform" : "facebook",
                "token" : local_token,
                "path" : pa,
                "message" : param,
                "api_key" : api_key
            }
        });
    },
    get : function() {
        var api_key = "key12345";
        var obj = JSON.parse(localStorage.getItem("tokens-facebook"));
        var local_token = obj[0].access_token;

        function success(data) {
            var userid = JSON.parse(data).id;
            if (userid != null && userid != undefined) {
                var mybook = JSON.parse(data);
                var alertMsg = "My Profile \n id:" + userid + '\n' +"homepage: " + mybook.link + '\n' +"first_name: " + mybook.first_name + '\n' +"last_name: " + mybook.last_name + '\n' +"gender: " + mybook.gender + '\n';
                alert(alertMsg);
            } else {
                console.log(str);
                alert(str);
            }
        };

        function error(data) {
            var str = responseString;
            console.log("Response (facebook):");
            console.log(str);
            alert(str);
        }


        $.ajax({
            url : "publishStatus",
            type : "GET",
            success : success,
            error : error,
            data : {
                "platform" : "facebook",
                "token" : local_token,
                "api_key" : api_key
            }
        });
    }
};

$(app.ready);
