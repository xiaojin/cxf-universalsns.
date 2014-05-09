var hasToken = false;
var showall = {
    ready : function() {
        var name = $("body").attr("class");
        console.log(name);
        if (name === "twitter") {
            window.location.href=conf.HOSTURL + "/cxf/service/twitterRequest/";
            // // authRequest(conf.TWITTER);
            // // oauthV1.oauthv1Search();
// 
            // function success(data) {
                // console.log(data);
                // // social.tool.loading.hide();
                // // var errorCode = $.xml2json(data).errorCode;
                // // if (errorCode === undefined) {
                    // // link.getFeed();
                // // } else {
                    // // alert(error + '\n' + $.xml2json(data).message);
                // // }
            // }
// 
            // function error(jqXHR, textStatus, errorThrown) {
                // console.log(errorThrown);
            // }
// 
            // function before(jqXHR) {
                // social.tool.loading.show();
            // }
// 
            // //getInfo
// 
            // $.ajax({
                // // url : conf.HOSTURL + "/cxf/service/twitterRequest/",
                // url : conf.HOSTURL + '/requesttest',
                // type : "GET",
                // success : success,
                // beforeSend : before,
                // contentType : "application/json",
                // error : error,
            // });
        } else if (name === "facebook") {
             window.location.href=conf.HOSTURL + "/cxf/service/linkedinRequest/";

        } else if (name === "googleplus") {

        } else if (name === "linkedin") {

        } else {
            console.log("Not Supported");
        }

    }
};

$(showall.ready);
