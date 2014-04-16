
var app = {
	ready : function() {
		app.bind();
		var obj = JSON.parse(localStorage.getItem("tokens-google"));
        if(obj!==null&&obj!==undefined){
            var current =moment().unix();
            if(current > obj.expires_at){
                app.initSigin();
                localStorage.removeItem("tokens-google");
            }else
            {
                 $("#google-token").text(obj.access_token);
            }
        }else
        {       
            app.initSigin();
        }
	},
	initSigin:function(){
	        oauth_google.signBtnID="signinButton";
            oauth_google.requestvisibleactions='http://schemas.google.com/AddActivity';
            oauth_google.scope='https://www.googleapis.com/auth/plus.login';
            oauth_google.cookiepolicy='single_host_origin';
            oauth_google.callBackFun = app.getTokenSuccess;
            authRequest(conf.GOOGLEPLUS);      
	},
    getTokenSuccess:function(){
        var obj = JSON.parse(localStorage.getItem("tokens-google"));
        var local_token = obj.access_token;
        $("#google-token").text(local_token);
    },
	getToken: function(){
		var obj = JSON.parse(localStorage.getItem("tokens-google"));
		var local_token = obj.access_token;
		$("#google-token").text(local_token);
	},


	bind : function() { 
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
	success : function(data) {
		console.log("Response (google):");
		console.log(data);
		if(data.id !=undefined && data.id!=""){
			alert("id: "+data.id+"\n"+"name: "+data.displayName);
		}
	},
	error : function(responseString) {

		var str = responseString;
		console.log("Response (google):");
		console.log(str);
		alert("You failed to post yet");

		
	},

	post : function() {
		var api_key = "123456";
		var obj = JSON.parse(localStorage.getItem("tokens-google"));
		var local_token = obj.access_token;
		$("#google-token").val(local_token);
		$.ajax({
			url : "/cxf/service/profile/",
			type : "POST",
            contentType:"application/json",
            dataType: 'json',
			beforeSend : app.beforeSend,
			complete : app.complete,
			success : app.success,
			error : app.error,
			data:JSON.stringify({ApiEntity:{platform: 'googleplus', apiKey: api_key,accessToken: local_token,paramter: 'param'}})
		});
	},

    insertMoments : function() {
      var obj = JSON.parse(localStorage.getItem("tokens-google"));
      var local_token = obj.access_token;
      var api_key = "123456";
        var param = JSON.stringify({
            "target": { "url": "https://developers.google.com/+/plugins/snippet/examples/thing" },
            "type": "http://schemas.google.com/AddActivity"});
        $.ajax({
            url : "/cxf/service/message/",
            type : "POST",
            success : app.success,
            error : app.error,
            dataType: 'json',
            contentType:"application/json",
            data:JSON.stringify({ApiEntity:{platform: 'googleplus', apiKey: api_key,accessToken: local_token,paramter: param}})
        });
    }


};

$(app.ready);
