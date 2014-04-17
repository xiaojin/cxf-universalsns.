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
        var sourceUrl = "https://api.linkedin.com/v1/people/~/network/updates/key=" + updateKey + "/update-comments";
        var accesstoken = JSON.parse(localStorage.getItem("takends-linkedin")).access_token;
        var url = sourceUrl + '?' + "oauth2_access_token=" + accesstoken;

        function success(data) {
            console.log(data);
            social.tool.loading.hide();
            var error = $.xml2json($(data)[2]).error_code;
            if (error === undefined) {
                link.getFeed();
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

        //getInfo

        $.ajax({
            url : "/cxf/service/comments/",
            type : "POST",
            success : success,
            beforeSend : before,
            contentType : "application/json",
            error : error,
            data : JSON.stringify({
                ApiEntity : {
                    tokenURL : escape(url),
                    platform : "linkedin",
                    apiKey : '123456',
                    linkedinEntity : {
                        message : myMessage
                    }
                }
            })
        });

    },

    getFeed : function(pauth_information) {
        $(".feed-entry").remove();
        var sourceUrl = "https://api.linkedin.com/v1/people/~/network/updates";
        var accesstoken = JSON.parse(localStorage.getItem("takends-linkedin")).access_token;
        var url = sourceUrl + '?scope=self&' + "oauth2_access_token=" + accesstoken;

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

        //getInfo

        $.ajax({
            url : "/cxf/service/profile/",
            type : "POST",
            success : success,
            beforeSend : before,
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
    },

    //count=1&start=5
    listLinkedPeople : function(oauth_information) {
        var sourceUrl = "https://api.linkedin.com/v1/companies/162479:(id,name,description,company-type,ticker,website-url)";
        var accesstoken = JSON.parse(localStorage.getItem("takends-linkedin")).access_token;
        var url = sourceUrl + '?' + "oauth2_access_token=" + accesstoken;

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

        //getInfo

        $.ajax({
            url : "/cxf/service/profile/",
            type : "POST",
            success : success,
            contentType : "application/json",
            error : error,
            beforeSend : before,
            data : JSON.stringify({
                ApiEntity : {
                    tokenURL : escape(url),
                    platform : "linkedin",
                    apiKey : '123456'
                }
            })
        });
    },
    shareCommand : function() {
        var sourceUrl = "https://api.linkedin.com/v1/companies/2414183/shares";
        var parameters = $("#commandValue").val();
        if (parameters == null || parameters == undefined) {
            parameters = "Hello, from our social framework";
        }
        var accesstoken = JSON.parse(localStorage.getItem("takends-linkedin")).access_token;
        var url = sourceUrl + '?' + "oauth2_access_token=" + accesstoken;
        function success(data) {
             social.tool.loading.hide();
            var comment = $.xml2json($(data)[2]);
             var error = $.xml2json($(data)[2]).error_code;
            if (error === undefined) {
            if (comment.update - url != undefined) {
                var mycomment = "<label class='alert alert-success comment-style'>" + $("#commandValue").val() + "</label>";
                $("#comment").append(mycomment);
            } else {
                // $("#comment").html(comment.message);
                alert(comment.message);
            }
            }else {
                alert(error + '\n' + $.xml2json($(data)[2]).message);
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

        function before(jqXHR) {
            social.tool.loading.show();
        }

        //getInfo

        $.ajax({
            url : "/cxf/service/message/",
            type : "POST",
            contentType : "application/json",
            success : success,
            error : error,
            beforeSend : before,
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
    },
    oauthLinkedIn : function() {
        var lineninToken = JSON.parse(localStorage.getItem("takends-linkedin"));
        if (lineninToken != null && lineninToken.access_token != undefined) {
            $("#linkedin-token").html(lineninToken.access_token);
        } else {
            authRequest(conf.LINKEDIN);
        }

    },
    ready : function() {
        $("#linkedin-token").html("");
        $("#googleURL").click(function() {
            window.location.href = conf.GOOGLE_CALLBACK;
        });
        var lineninToken = JSON.parse(localStorage.getItem("takends-linkedin"));
        if (lineninToken != null && lineninToken.access_token != undefined) {
            $("#linkedin-token").html(lineninToken.access_token);
        }
        $("#linkedin-get-token").click(link.oauthLinkedIn);
        $("#linkedinComp").click(link.listLinkedPeople);
        $("#sharecommand").click(link.shareCommand);
        $("#commandLine").hide();

        $("#linkedin-get-feed").click(link.getFeed);

        oauthV1.oauthv1Search();
    }
};

$(link.ready);
