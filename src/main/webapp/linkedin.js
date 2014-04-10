var hasToken = false;
var link = {
    //count=1&start=5
    listLinkedPeople : function(oauth_information) {
        var sourceUrl = "https://api.linkedin.com/v1/companies/2414183:(id,name,description,company-type,ticker,website-url)";
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
       var parameters = $("#commandValue").val();
       if(parameters == null || parameters == undefined)
       {
           parameters ="Hello, from our social framework";
       }
       var accesstoken =JSON.parse(localStorage.getItem("takends-linkedin")).access_token;
        var url = sourceUrl+'?'+"oauth2_access_token="+accesstoken;
        function success(data) {
             var msg = '', isxml = true, backurl='';
            try {
                backurl = $(data).find("update").text();
                
            } catch(e) {
                isxml = false;
            }
            if (isxml) {                           
                console.log(data);
                alert(data);
                url = $(data).find("update-url").text();
                $("#commandLine").show();
                $("#commandLine").click(function(){
                    window.open("https://www.linkedin.com/company/devtestco","_blank");
                });
            } else {
                alert(data);
            }


            
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
                    "comment": parameters,
                    "title":'SDC Soical Framework'
                }),
                "api_key":api_key,
                "type":"POST"
                },
        });
        
    },
    oauthLinkedIn : function() {
        var lineninToken = JSON.parse(localStorage.getItem("takends-linkedin"));
        if(lineninToken!=null && lineninToken.access_token!=undefined){
             $("#linkedin-token").text(lineninToken.access_token);
        }else
        {
             authRequest(conf.LINKEDIN);
        }
       
    },
    ready : function() {
         var lineninToken = JSON.parse(localStorage.getItem("takends-linkedin"));
         if(lineninToken!=null && lineninToken.access_token!=undefined){
             $("#linkedin-token").text(lineninToken.access_token);
         }
        $("#linkedin-get-token").click(link.oauthLinkedIn);
        $("#linkedinComp").click(link.listLinkedPeople);
        $("#sharecommand").click(link.shareCommand);
        $("#commandLine").hide();
        
        oauthV1.oauthv1Search();
    }
};

$(link.ready);
