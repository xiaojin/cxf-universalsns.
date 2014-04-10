var overall = {
    //count=1&start=5
    listLinkedPeople : function(oauth_information) {
        var sourceUrl = document.getElementById("LinkedInSource-url").value;
        var accesstoken =JSON.parse(localStorage.getItem("twitteroauth")).access_token;
        var url = sourceUrl+'?'+"oauth2_access_token="+accesstoken;
        
        function success(data) {
            console.log(data);
            alert(data);
            // window.location.href = "http://localhost:9090/overall.html";
        }

        function error(jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
        }
        //getInfo
		var api_key = "key12345";
		$.ajax({
			url : "getInfo",
			type : "POST",
			success : success,
			error : error,
			data : {
				"platform" : "linkedin",
				"token" : '',
				 type : "GET",
				"path" : url,
				"message" : "",
				"api_key":api_key
			}
		});
    },
    shareCommand :function(){
       var sourceUrl = "https://api.linkedin.com/v1/companies/2414183/shares";
       var parameters = "";
       var accesstoken =JSON.parse(localStorage.getItem("takends-linkedin")).access_token;
        var url = sourceUrl+'?'+"oauth2_access_token="+accesstoken;
        function success(data) {
            console.log(data);
            alert(data);
            // window.location.href = "http://localhost:9090/overall.html";
        }

        function error(jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
        }
        //getInfo
        var api_key = "key12345";
        $.ajax({
            url : "getInfo",
            type : "POST",
            success : success,
            error : error,
            data : {
                "platform" : "linkedin",
                "token" : localStorage.getItem("authInfo"),
                "path" :url,
                "message" : JSON.stringify({
                    "visibility": { "code": "anyone" },
                    "comment": "Hello,social network shareing information"
                }),
                "api_key":api_key,
                "type":"POST"
                },
        });
        
    },
    listFav : function(oauth_information) {
        oauth.reset();
        var sourceUrl = document.getElementById("source-url").value;
        var parameters = "count=10";
        var action = "Get";

        var url = oauth.sign({
            action : action,
            path : sourceUrl,
            parameters : parameters,
            signatures : oauth_information
        }).signed_url;
        console.log(url);

        function success(data) {
            console.log(data);
            alert("Fav List Success");
            alert(data);
            window.location.href = "http://localhost:9090/overall.html";
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


    oauthLinkedIn : function() {
        authRequest(conf.LINKEDIN);
    },
    oauthTwitter : function() {
        authRequest(conf.TWITTER);
    },
    oauthGoogle : function() {
        authRequest(conf.GOOGLEPLUS);
    },
    oauthFacebook : function() {
        window.location.href="facebook.html";
      // authRequest(conf.FACEBOOK);
    },
    ready : function() {
        $("#authLinkedIn").click(overall.oauthLinkedIn);
        $("#authTwitter").click(overall.oauthTwitter);
        $("#authGoogle").click(overall.oauthGoogle);
        $("#authFacebook").click(overall.oauthFacebook);
        $("#sharecommand").click(overall.shareCommand);
        oauthV1.oauthv1Search();
    }
};

$(overall.ready);
