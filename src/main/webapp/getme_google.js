var app = {
	ready : function() {
		app.bind();
		jso_configure({
            "google" : {
                client_id : "813483280675-h879tteoivpnmfeujgf2lg1j9t2jk9n7.apps.googleusercontent.com",
                redirect_uri : "http://localhost:9090/google.html",
                authorization : "https://accounts.google.com/o/oauth2/auth",
                isDefault : true
            }
        });
		//jso_wipe();
		jso_ensureTokens({
            "google" : ["https://www.googleapis.com/auth/plus.me"],
        });

	},
	
	getToken: function(){
		var obj = JSON.parse(localStorage.getItem("tokens-google"));
		var local_token = obj[0].access_token;
		$("#google-token").text(local_token);
		
	},

	bind : function() {
	
		$("#post").click(function() {
			app.post();
		});
		
		$("#google-get-me").click(function() {
			app.post();
		});
		$("#google-get-token").click(function() {
			app.getToken();
		});
		$("#google-insert").click(function(){
		    app.insertMoments();
		});
	},

	beforeSend : function() {

	},
	complete : function(responseString) {
		var str = responseString;
	},
	success : function(responseString) {
		var str = responseString;
		console.log("Response (google):");
		console.log(str);
		alert(str);

	},
	error : function(responseString) {

		var str = responseString;
		console.log("Response (google):");
		console.log(str);
		alert( str);

		
	},

	post : function() {
		var api_key = "key12345";
		var pa = $("#path").val();
		var param = $("#parameter").val();
		var obj = JSON.parse(localStorage.getItem("tokens-google"));
		var local_token = obj[0].access_token;
		$("#google-token").val(local_token);
		$.ajax({
			url : "publishStatus",
			type : "POST",
			beforeSend : app.beforeSend,
			complete : app.complete,
			success : app.success,
			error : app.error,
			data : {
				"platform" : "google",
				"token" : local_token,
				"path" : pa,
				"api_key":api_key
				
			}
		});
	},

    insertMoments : function() {
      var obj = JSON.parse(localStorage.getItem("tokens-google"));
      var local_token = obj[0].access_token;
      var api_key = "key12345";
        $.ajax({
            url : "insertMoments",
            type : "POST",
            success : app.success,
            error : app.error,
            data : {
                "platform" : "google",
                "token" : local_token,
                "message" :JSON.stringify({
                    "target": { "url": "https://developers.google.com/+/plugins/snippet/examples/thing" },
                    "type": "http://schemas.google.com/AddActivity"
                }),
                "api_key" : api_key
            }
        });
    }


};

$(app.ready);
