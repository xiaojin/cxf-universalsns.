var app = {
    ready : function() {
        app.bind();
        jso_configure({
            "facebook" : {
                client_id : "1455916827976099",
                redirect_uri : "http://127.0.0.1:9090/facebook.html",
                authorization : "https://www.facebook.com/dialog/oauth",
                presenttoken : "qs"
            }
        });
        jso_ensureTokens({
            "facebook" : ["publish_actions", "read_stream"]
        });
        jso_dump();
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
        var api_key = "123456";
        var param = $("#parameter").val();
        var obj = JSON.parse(localStorage.getItem("tokens-facebook"));
        var local_token = obj[0].access_token;

        function success(data) {
            var status = data.id;
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
            url : "/cxf/service/message/",
            type : "POST",
            success : success,
            error : error,
            dataType: 'json',
            contentType:"application/json",
            data:JSON.stringify({ApiEntity:{platform: 'facebook', apiKey: api_key,accessToken: local_token,paramter: param}})
        });
    },
    get : function() {
        var api_key = "123456";
        var obj = JSON.parse(localStorage.getItem("tokens-facebook"));
        var local_token = obj[0].access_token;

        function success(data) {
            var userid = data.id;
            if (userid != null && userid != undefined) {
                var alertMsg = "My Profile \n id:" + userid +  '\n' +"first_name: " + data.first_name + '\n' +"last_name: " + data.last_name + '\n' +"gender: " + data.gender + '\n';
                alert(alertMsg);
            } else {
                alert("no id");
            }
        };

        function error(data) {
            var str = data.responseText;
            console.log("Response (facebook):");
            console.log(str);
            alert(str);
        }


        $.ajax({
            url : "/cxf/service/profile/",
            type : "POST",
            success : success,
            dataType: 'json',
            contentType:"application/json",
            error : error,
            data:JSON.stringify({ApiEntity:{platform: 'facebook', apiKey: api_key,accessToken: local_token,paramter: 'paramd'}})
        });
    }
};

$(app.ready);
