var hasToken = false;
var link = {
    buildfeedlist : function(obj, $parent) {
        var $feedItem = $(".myfeedli");
        var $copy = $feedItem.clone();

        var update_comments = obj.update_comments.update_comment;
        $copy.find(".linkedin-name a").html(obj.update_content.person.first_name + " " + obj.update_content.person.last_name);
        $copy.find(".linkedin-content span").html(obj.update_content.person.current_share.comment);
        var $itemParent = $copy.find('.feed-list');

        if ($.isArray(update_comments)) {
            $.each(update_comments, function(index, val) {
                var $item = $itemParent.find(".feed-item");
                var $copyitem = $item.clone();
                var timestamp = moment(parseInt(val.timestamp)).fromNow();
                console.log("feeed" + index);
                $copyitem.find(".feed-person").html(val.person.first_name + " " + val.person.last_name);
                $copyitem.find(".feed-text").html(val.comment);
                $copyitem.find(".timestamp").html(timestamp);
                $copyitem.find(".feed-photo").attr("src", val.person.picture_url);
                $copyitem.addClass("item-entry").removeClass("feed-item hidden");
                $itemParent.append($copyitem);
            });
        } else if (update_comments) {
            var $item = $itemParent.find(".feed-item");
            var $copyitem = $item.clone();
            var timestamp = moment(parseInt(update_comments.timestamp)).fromNow();
            $copyitem.find(".feed-person").html(update_comments.person.first_name + " " + update_comments.person.last_name);
            $copyitem.find(".feed-text").html(update_comments.comment);
            $copyitem.find(".timestamp").html(timestamp);
            $copyitem.find(".feed-photo").attr("src", update_comments.person.picture_url);
            $copyitem.addClass("item-entry").removeClass("feed-item hidden");
            $itemParent.append($copyitem);
        }
        $copy.addClass("feed-entry").removeClass("myfeedli hidden");
        $copy.data("linkedinfeed", obj);
        $parent.append($copy);

    },

    postFeed : function(e) {
        var $target = $(e.target);
        var obj = $(this).closest(".feed-entry").data("linkedinfeed");
        var myComment = $target.parent().parent().find(".commentValue").val();
        if (myComment === "" || myComment === undefined) {
            alert("Say something");
        } else {
            link.uploadFeed(obj.update_key, myComment);
        }
    },
    uploadFeed : function(key, message) {
        var updateKey = key;
        var myMessage = message;
        var param = "key=" + updateKey ;
        var accesstoken = JSON.parse(localStorage.getItem("tokens-linkedin")).access_token;

        function success(data) {
            console.log(data);
            social.tool.loading.hide();
            var errorCode = $.xml2json(data).errorCode;
            if (errorCode === undefined) {
                link.getFeed();
            } else {
                alert(error + '\n' +$.xml2json(data).message);
            }
        }

        function error(jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
        }

        function before(jqXHR) {
            social.tool.loading.show();
        }

        //getInfo

        $.ajax({
            url : conf.HOSTURL+"/cxf/service/message/",
            type : "POST",
            success : success,
            beforeSend : before,
            contentType : "application/json",
            dataType : 'xml',
            error : error,
            data : JSON.stringify({
                ApiEntity : {
                    accessToken : accesstoken,
                    platform : "linkedin",
                    apiKey : '123456',
                    linkedinEntity : {
                        message : myMessage,
                        parameters:param
                    }
                }
            })
        });

    },

    getFeed : function(pauth_information) {
        $(".feed-entry").remove();
        var accesstoken = JSON.parse(localStorage.getItem("tokens-linkedin")).access_token;
        function success(data) {
            social.tool.loading.hide();
            console.log(data);
            var error = $.xml2json($(data)[2]).error_code;
            if (error === undefined) {
                var updates = $.xml2json($(data)[2]).update;
                var current = moment().unix();
                if ($.isArray(updates)) {
                    $.each(updates, function(index, val) {
                        if (val.update_type === "SHAR") {
                            link.buildfeedlist(val, $("#myfeeds"));
                        }
                    });
                } else if (updates) {
                    if (updates.update_type === "SHAR") {
                        link.buildfeedlist(updates, $("#myfeeds"));
                    }
                }
                $(".postShare").click(link.postFeed);
            } else {
                alert(error + '\n' + $.xml2json($(data)[2]).message);
            }

        }

        function error(jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
        }

        function before(jqXHR) {
            social.tool.loading.show();
        }

        //getInfo scope=self

  
        $.ajax({
            url : conf.HOSTURL+"/cxf/service/feed_get/",
            type : "POST",
            success : success,
            beforeSend : before,
            contentType : "application/json",
            error : error,
            data : JSON.stringify({
                ApiEntity : {
                    accessToken : accesstoken,
                    platform : "linkedin",
                    apiKey : '123456',
                    linkedinEntity : {
                        personID : "me",
                        parameters:""
                    }
                }
            })
        }); 

    },

    
    listCompanyProfile : function(oauth_information) {
        // var sourceUrl = "https://api.linkedin.com/v1/companies/162479:(id,name,description,company-type,ticker,website-url)";
        var accesstoken = JSON.parse(localStorage.getItem("tokens-linkedin")).access_token;
        // var url = sourceUrl + '?' + "oauth2_access_token=" + accesstoken;
        var company = 162479;
        function success(data) {
            console.log(data);
            social.tool.loading.hide();
             var error = $.xml2json($(data)[2]).error_code;
            if (error === undefined) {
            var company = $.xml2json($(data)[2]);
            if (company.id != undefined) {
                $("#companyid").html(company.id);
                $("#companyName").html(company.name);
                $("#companyurl").html(company.website_url);
                $("#companytype").html(company.company_type.name);
                $("#companydesc").html(company.description);
            } else {
                $("#companyid").html("");
                $("#companyName").html("");
                $("#companyurl").html("");
                $("#companytype").html("");
                $("#companydesc").html(company.message);
                $("#companydesc").addClass("alert alert-danger");

            }
            }else {
                alert(error + '\n' + $.xml2json($(data)[2]).message);
            }
        }

        function error(jqXHR, textStatus, errorThrown) {
             social.tool.loading.hide();
            console.log(errorThrown);
        }

        function before(jqXHR) {
            social.tool.loading.show();
        }


        $.ajax({
            url : conf.HOSTURL+"/cxf/service/companyProfile/",
            type : "POST",
            success : success,
            contentType : "application/json",
            error : error,
            beforeSend : before,
            data : JSON.stringify({
                ApiEntity : {
                    accessToken : accesstoken,
                    platform : "linkedin",
                    apiKey : '123456',
                    linkedinEntity : {
                        parameters : '(id,name,description,company-type,ticker,website-url)',
                        companyID:company
                    }
                }
            })
        }); 

    },
    shareComment : function() {
        var company = 2414183;
        var comments = $("#commandValue").val();
        if (comments == null || comments == undefined) {
            comments = "Hello, from our social framework";
        }
        var accesstoken = JSON.parse(localStorage.getItem("tokens-linkedin")).access_token;
        function success(data) {
             social.tool.loading.hide();
            var comment = $.xml2json($(data)[2]);
             var error = $.xml2json($(data)[2]).error_code;
            if (error === undefined) {
            if (comment.update_url != undefined) {
                var mycomment = "<label class='alert alert-success comment-style'>" + $("#commandValue").val() + "</label>";
                $("#comment").append(mycomment);
            } else {
                // $("#comment").html(comment.message);
                alert(comment.message);
            }
            }else {
                alert(error + '\n' + $.xml2json($(data)[2]).message);
            }
        }

        function error(jqXHR, textStatus, errorThrown) {
            social.tool.loading.hide();
            console.log(errorThrown);
        }

        function before(jqXHR) {
            social.tool.loading.show();
        }

        //getInfo

        $.ajax({
            url : conf.HOSTURL+"/cxf/service/comments/",
            type : "POST",
            contentType : "application/json",
            success : success,
            error : error,
            beforeSend : before,
            data : JSON.stringify({
                ApiEntity : {
                    platform : "linkedin",
                    accessToken : accesstoken,
                    apiKey : '123456',
                    linkedinEntity : {
                        message : JSON.stringify({
                            "visibility" : {
                                "code" : "anyone"
                            },
                            "comment" : comments,
                            "title" : 'SDC Soical Framework'
                        }),
                        companyID:company
                    }
                }
            })
        });
    },
    
    getUserProfile :function(){
        var accesstoken = JSON.parse(localStorage.getItem("tokens-linkedin")).access_token;
        var company = 162479;
        function success(data) {
            console.log(data);
            social.tool.loading.hide();
            var error = $.xml2json(data).error_code;
            if (error === undefined) {
            var user = $.xml2json(data);
            if (user != undefined) {
                $("#user-id").html(user.id);
                $("#name").html(user.username);
                $("#user-desc").html(user.description);
                $("#link").html(user.link);
            } else {
                $("#user-id").html();
                $("#name").html();
                $("#user-desc").html();
                $("#link").html();

            }
            }else {
                alert(error + '\n' + $.xml2json(data).message);
            }
        }

        function error(jqXHR, textStatus, errorThrown) {
             social.tool.loading.hide();
            console.log(errorThrown);
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
                    accessToken : accesstoken,
                    platform : "linkedin",
                    apiKey : '123456',
                    linkedinEntity : {
                        personID : "me",
                        parameters : '(id,first-name,last-name,headline,site_standard_profile_request)'
                    }
                }
            })
        });         
    },
    oauthLinkedIn : function() {
        var lineninToken = JSON.parse(localStorage.getItem("tokens-linkedin"));
        if (lineninToken != null && lineninToken.access_token != undefined) {
            $("#linkedin-token").html(lineninToken.access_token);
        } else {
            // authRequest(conf.LINKEDIN);
            var sns = SNS();
            sns.linkedin_token(function(){
            // var ss = localStorage.getItem("tokens-facebook");
            var linkedinToken = JSON.parse(localStorage.getItem("tokens-linkedin"));
            $("#linkedin-token").html(linkedinToken.access_token);
        });
        }

    },
    ready : function() {
        $("#linkedin-token").html("");
        $("#googleURL").click(function() {
            window.location.href = conf.GOOGLE_CALLBACK;
        });
        var lineninToken = JSON.parse(localStorage.getItem("tokens-linkedin"));
        if (lineninToken != null && lineninToken.access_token != undefined) {
            $("#linkedin-token").html(lineninToken.access_token);
        }
        $("#linkedin-get-token").click(link.oauthLinkedIn);
        $("#linkedin-get-profile").click(link.getUserProfile);
        $("#linkedinComp").click(link.listCompanyProfile);
        $("#sharecommand").click(link.shareComment);
        $("#commandLine").hide();

        $("#linkedin-get-feed").click(link.getFeed);

        // oauthV1.oauthv1Search();
    }
};

$(link.ready);
