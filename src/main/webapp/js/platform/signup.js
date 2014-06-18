var signUp = {
    keyup : function($item) {
        var $form = $item.closest('.form'), $group = $item.closest('.input-group'), $addon = $group.find('.input-group-addon'), $icon = $addon.find('span'), state = false;
        if (!$group.data('validate')) {
            state = $item.val() ? true : false;
        } else if ($group.data('validate') == "email") {
            state = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test($item.val());
        } else if ($group.data('validate') == 'phone') {
            state = /^[(]{0,1}[0-9]{3}[)]{0,1}[-\s\.]{0,1}[0-9]{3}[-\s\.]{0,1}[0-9]{4}$/.test($item.val());
        } else if ($group.data('validate') == "length") {
            state = $item.val().length >= $group.data('length') ? true : false;
        } else if ($group.data('validate') == "number") {
            state = !isNaN(parseFloat($item.val())) && isFinite($item.val());
        }

        if (state) {
            $addon.removeClass('danger');
            $addon.addClass('success');
            $icon.attr('class', 'glyphicon glyphicon-ok');
        } else {
            $addon.removeClass('success');
            $addon.addClass('danger');
            $icon.attr('class', 'glyphicon glyphicon-remove');
        }

        if ($form.find('.input-group-addon.danger').length == 0) {
            $form.find('#uploadbtn').prop('disabled', false);
        } else {
            $form.find('#uploadbtn').prop('disabled', true);
        }
    },
    uploadinfo : function() {
        function onSuccess(data) {
            $("#register-form").hide();
            $("#udid").show();
            $("#udid").html("");
            $("#udid").html(data);
            console.log(data);
        };
        function onError(data) {
            console.log(data);
        };

        var name = $(".form").find("#user-name").val();
        var password = $(".form").find("#password").val();
        var email = $(".form").find("#validate-email").val();
        var phone = $(".form").find("#validate-phone").val();
        var callbackURL = $(".form").find("#callbackURL").text();
        var clientType = $(".form").find("#validate-select :selected").val();
        $.ajax({
            url : conf.HOSTURL + "/cxf/gate/signup/new?name=xiao",
            type : "POST",
            success : function(data) {
                    $("#register-form").hide();
                    $("#udid").show();
                    $("#udid").html("");
                    $("#udid").html(data);
                    console.log(data);
            },
            contentType : "application/json",
            error : onError,
            data : JSON.stringify({
                SignupEntity : {
                    name : name,
                    phone : phone,
                    email : email,
                    clientType : clientType,
                    callbackUrl : callbackURL,
                }
            })
        });
    },
    ready : function() {
        $('.input-group input[required], .input-group textarea[required], .input-group select[required]').on('keyup change', function() {
            signUp.keyup($(this));
        });
        $('.input-group input[required], .input-group textarea[required], .input-group select[required]').trigger('change');
        $("#uploadbtn").click(function() {
            signUp.uploadinfo();
        });
    }
};
$(signUp.ready);
