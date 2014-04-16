var hasToken = false;
var link = {
    //count=1&start=5
    listLinkedPeople : function(oauth_information) {
        var sourceUrl = "https://api.linkedin.com/v1/companies/162479:(id,name,description,company-type,ticker,website-url)";
        var accesstoken =JSON.parse(localStorage.getItem("takends-linkedin")).access_token;
        var url = sourceUrl+'?'+"oauth2_access_token="+accesstoken;
        
        function success(data) {
            console.log(data);
            var company =$.xml2json($(data)[2]);
            if(company.id !=undefined){
                $("#companyid").html(company.id);
                $("#companyName").html(company.name);
                $("#companyurl").html(company.website_url);
                $("#companytype").html(company.company_type.name);
                $("#companydesc").html(company.description);
             }
             else{
                 $("#companyid").html("");
                 $("#companyName").html("");
                 $("#companyurl").html("");
                 $("#companytype").html("");
                 $("#companydesc").html(company.message);
                 $("#companydesc").addClass("alert alert-danger");
                 
             }
            }
    

        function error(jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
        }
        //getInfo
        
             $.ajax({
                url : "/cxf/service/profile/",
                type : "POST",
                success : success,
                contentType : "application/json",
                error : error,
                data : JSON.stringify({
                    ApiEntity : {
                        tokenURL : escape(url),
                        platform : "linkedin",  
                        apiKey : '123456'                 
                    }
                })
            }); 
        
        // var api_key = "key12345";
        // $.ajax({
            // url : "getInfo",
            // type : "POST",
            // success : success,
            // error : error,
            // data : {
                // "platform" : "linkedin",
                // "token" : '',
                 // type : "GET",
                // "path" : url,
                // "message" : "",
                // "api_key":api_key
            // }
        // });
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
             var comment =$.xml2json($(data)[2]);
            if(comment.update-url !=undefined){
                var mycomment = "<label class='alert alert-success comment-style'>"+$("#commandValue").val()+"</label>";
                $("#comment").append(mycomment);
            }else
            {
                // $("#comment").html(comment.message);
                alert(comment.message);
            }                          
                // console.log(data);
                // alert(data);
                // url = $(data).find("update-url").text();
                // $("#commandLine").show();
                // $("#commandLine").click(function(){
                    // window.open("https://www.linkedin.com/company/devtestco","_blank");
                // });


            
            // window.location.href = "http://localhost:9090/overall.html";
        }

        function error(jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
        }
        //getInfo
        


        $.ajax({
            url : "/cxf/service/message/",
            type : "POST",
            contentType : "application/json",
            success : success,
            error : error,
            data : JSON.stringify({
                ApiEntity : {
                    platform : "linkedin",
                    accessToken : localStorage.getItem("authInfo"),
                    apiKey : '123456',
                    tokenURL : url,
                    linkedinEntity : {
                        message : JSON.stringify({
                            "visibility" : {
                                "code" : "anyone"
                            },
                            "comment" : parameters,
                            "title" : 'SDC Soical Framework'
                        })
                    }
                }
            })
        });


        
//         
        // var api_key = "key12345";
        // $.ajax({
            // url : "getInfo",
            // type : "POST",
            // success : success,
            // error : error,
            // data : {
                // "platform" : "linkedin",
                // "token" : localStorage.getItem("authInfo"),
                // "path" :url,
                // "message" : JSON.stringify({
                    // "visibility": { "code": "anyone" },
                    // "comment": parameters,
                    // "title":'SDC Soical Framework'
                // }),
                // "api_key":api_key,
                // "type":"POST"
                // },
        // });
        
    },
    oauthLinkedIn : function() {
        var lineninToken = JSON.parse(localStorage.getItem("takends-linkedin"));
        if(lineninToken!=null && lineninToken.access_token!=undefined){
             $("#linkedin-token").html(lineninToken.access_token);
        }else
        {
             authRequest(conf.LINKEDIN);
        }
       
    },
    ready : function() {
         $("#linkedin-token").html("");
         $("#googleURL").click(function(){
           window.location.href=conf.GOOGLE_CALLBACK; 
        });
         var lineninToken = JSON.parse(localStorage.getItem("takends-linkedin"));
         if(lineninToken!=null && lineninToken.access_token!=undefined){
             $("#linkedin-token").html(lineninToken.access_token);
         }
        $("#linkedin-get-token").click(link.oauthLinkedIn);
        $("#linkedinComp").click(link.listLinkedPeople);
        $("#sharecommand").click(link.shareCommand);
        $("#commandLine").hide();
        
        oauthV1.oauthv1Search();
    }
};

$(link.ready);
