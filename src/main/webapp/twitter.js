var app = {
	ready : function() {
		app.bind();
		app.getToken();
	},
	
	getToken: function(){
		$.ajax({
			url : "getAccessTokenTwitter",
//			url : "AccessToken",
			type : "POST",
//			type : "GET",
			beforeSend : app.beforeSendToken,
			complete : app.completeToken,
			success : app.successToken,
			error : app.errorToken,
			data : {
				"apikey" : "YpDGOaeVvNnp67NG2AC4lA",
				"apisecret" : "SbOAksXWflpwMNhfQ5vyVSIjfQeBzIeTHjz29zG40M"
			}
		});
		
	},

	bind : function() {
	
		$("#twitter-get-token").click(function (){
			app.getAccessToken();
		});
		$("#twitter-get").click(function (){
			app.getUserTimeline();
		});
	},

	getAccessToken:function(){
//		window.location = app.authUrl;
		$("#twitter-token").text(app.access_token);
	},
	
	beforeSendToken : function() {
		
	},
	completeToken : function(responseString) {
		var str = responseString;
	},
	successToken : function(responseString) {
		var str = responseString;
		console.log("Token :");
		console.log(str);
//		alert(str);
//		app.authUrl = str;
		var obj = jQuery.parseJSON(str);
		app.access_token = obj.access_token;
		app.access_token_type = obj.token_type;
		$("#twitter-token").text(app.access_token);
	},
	errorToken : function(responseString) {
		
		var str = responseString;
		console.log("Token :");
		console.log(str);
		alert( str);
		
		
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
	
	getUserTimeline:function(){
		var api_key = "key12345";
		$.ajax({
			url : "publishStatus",
			type : "POST",
			beforeSend : app.beforeSend,
			complete : app.complete,
			success : app.success,
			error : app.error,
			data : {
				"platform" : "twitter",
				"token" : app.access_token,
				"token_type" : app.access_token_type,
				"api_key":api_key
				
			}
		});
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
	}

};

$(app.ready);
