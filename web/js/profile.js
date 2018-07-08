function ChangeRedirect() {
    window.location.href = "http://localhost:8080/change_profile.jsp"
}

function ChangeEssential() {
    var username = $('#username-input').val();
    var email = $('#email-input').val();
    var success = true;
    var condition = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(email);
    if(username.length<8){
        animateElem(false,$('#username-input'));
        success = false;
    }else{
        $('#username-input').css('background-color',"white");
    }
    if(!condition){
        animateElem(condition,$('#email-input'));
        success = false;
    }else{
        $('#email-input').css('background-color',"white");
    }
    if(!success)
        return;
    try{
        $.ajax({
            url: "ModifyAccServlet",
            type: 'POST',
            data: {
                username: username,
                email: email,
                type: "essential"
            },
            success: function (data) {
                if(data==="success"){
                    animateElem(true,$('#username-input'));
                    animateElem(true,$('#email-input'));
                }else{
                    console.log(data)
                }
            }
        });
    } catch (err) {
        console.log(err.message)
    }

    console.log(username + " - " + email)
}

function changePass() {
    var oldPass = $('#old-password-input').val();
    var newPass = $('#new-password-input').val();
    var newPassRe = $('#repeat-password-input').val();
    var success = true;
    var condition = /(?=.*[a-zA-Z])(?=.*[0-9])(?=.{8,})/.test(newPass);

    if(!condition){
        animateElem(false,$('#new-password-input'));
        success = false;
    }else{
        $('#new-password-input').css('background-color',"white");
    }

    if(newPassRe!==newPass){
        animateElem(condition,$('#repeat-password-input'));
        success = false;
    }else{
        $('#repeat-password-input').css('background-color',"white");
    }

    if(!success)
        return;
    try{
        $.ajax({
            url: "ModifyAccServlet",
            type: 'POST',
            data: {
                old_pass: oldPass,
                new_pass: newPass,
                type: "pass_change"
            },
            success: function (data) {
                if(data==="success"){
                    animateElem(true,$('#old-password-input'));
                    animateElem(true,$('#new-password-input'));
                    animateElem(true,$('#repeat-password-input'));
                }else{
                    animateElem(false,$('#old-password-input'));
                    $('#new-password-input').css('background-color',"white");
                    $('#repeat-password-input').css('background-color',"white");
                    console.log(data)
                }
            }
        });
    } catch (err) {
        console.log(err.message)
    }
}

function setPass() {
    var newPass = $('#new-password-input').val();
    var newPassRe = $('#repeat-password-input').val();
    var success = true;
    var condition = /(?=.*[a-zA-Z])(?=.*[0-9])(?=.{8,})/.test(newPass);

    if(!condition){
        animateElem(false,$('#new-password-input'));
        success = false;
    }else{
        $('#new-password-input').css('background-color',"white");
    }

    if(newPassRe!==newPass){
        animateElem(condition,$('#repeat-password-input'));
        success = false;
    }else{
        $('#repeat-password-input').css('background-color',"white");
    }

    if(!success)
        return;
    try{
        $.ajax({
            url: "ModifyAccServlet",
            type: 'POST',
            data: {
                new_pass: newPass,
                type: "pass_set"
            },
            success: function (data) {
                if(data==="success"){
                    animateElem(true,$('#new-password-input'));
                    animateElem(true,$('#repeat-password-input'));
                }else{
                    animateElem(false,$('#new-password-input'));
                    $('#new-password-input').val("");
                    $('#repeat-password-input').val("");
                    $('#repeat-password-input').css('background-color',"white");
                    console.log(data)
                }
            }
        });
    } catch (err) {
        console.log(err.message)
    }
}


function animateElem(condition,elem) {
    var colorRGBA = "";
    if (condition) {
        colorRGBA = "62,232,68,";
    }else{
        colorRGBA = "245, 6, 78,"
    }
    var elem_opacity = 0;
    var id = setInterval(frame, 0.5);

    function frame() {
        if (elem_opacity >= 0.45) {
            clearInterval(id);
        } else {
            elem_opacity = elem_opacity + 0.005;
            elem.css('background-color', "rgba("+colorRGBA + elem_opacity + ")");
        }
    }
}