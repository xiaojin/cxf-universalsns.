var hasToken = false;
var showall = {
    ready : function() {
        var sns = SNS();
        sns.facebook_token(function(){
            var ss = localStorage.getItem("tokens-facebook");
            console.log(ss);
        });
    }
};

$(showall.ready);
