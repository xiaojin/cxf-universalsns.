var hasToken = false;
var showall = {
    bind : function() {
        var token = JSON.parse(localStorage.getItem("tokens-google"));
        var refresh_token = token.refresh_token;
        function success(data) {
             social.tool.loading.hide();
            var xmlDoc = $.parseXML(data),
            ss = JSON.parse(localStorage.getItem("tokens-google"));
            $xml = $(xmlDoc);
            var accessToken = $xml.find( "access_token" ).text()==null?"":$xml.find( "access_token" ).text();
            var tokenType = $xml.find( "token_type" ).text()==null?"":$xml.find( "token_type" ).text();
            var expires = $xml.find( "expires_in" ).text()==null?"":$xml.find( "expires_in" ).text();
            $("#older").html(ss.access_token);
            $("#newer").html(accessToken);
        }

        function error(jqXHR, textStatus, errorThrown) {
            social.tool.loading.hide();
            console.log(errorThrown);
        }

        function before(jqXHR) {
            social.tool.loading.show();
        }


        $.ajax({
            url : conf.GOOGLE_REFRESHTOKENSERVICE + "?refreshtoken=" + refresh_token,
            type : "GET",
            success : success,
            error : error,
            beforeSend : before,
        });
    },

    ready : function() {
        var ss = localStorage.getItem("tokens-google");
        $("#cutomer").click(function(){
            showall.bind();
        });
        if(ss!=null){
            
        }else
        {
                    var sns = SNS();
        sns.google_token(function(){
        var ss = localStorage.getItem("tokens-google");
        console.log(ss);
        });

        }

    }
};

$(showall.ready);
