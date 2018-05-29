function configureBtn(){
    var cond = similarityCheckCond&&asciiCheckCond&&numAndLetterCheckCond&&lenCheckCond&&passwordMatch
    document.getElementById("register-btn").disabled=!cond
    return cond
}

var emailTimeout = null
var usernameTimeout = null

var lenCheckCond = false
var asciiCheckCond = false
var numAndLetterCheckCond = false
var similarityCheckCond = false
var passwordMatch=true
var re_password_triggered=false

function setAllFalse(){
    lenCheckCond = false
    asciiCheckCond = false
    numAndLetterCheckCond = false
    similarityCheckCond = false
}

function setAllLabels(){
    setLenP(lenCheckCond)
    setASCIIP(asciiCheckCond)
    setNumAndLetterP(numAndLetterCheckCond)
    setSimilarityP(similarityCheckCond)
}


function checkLen(password){
    lenCheckCond = password.length > 7
}

function checkASCII(password) {
    asciiCheckCond = /^[A-Za-z][A-Za-z0-9]*$/.test(password)
}

function checkNumAndLetter(password){
    numAndLetterCheckCond = /\d/.test(password) && (/[a-z]/i.test(password) || /[A-Z]/i.test(password))
}

function checkSimiliarity(password){
    similarityCheckCond = !(password==document.getElementById("username-input").value)
}

function setHintById(condition, hint_text, hint_id){
    var inner_html = ""
    if(condition)
        inner_html = "<i class=\"material-icons hint-icons align-middle hint-accept\">check</i>"
    else
        inner_html = "<i class=\"material-icons hint-icons align-middle hint-reject\">close</i>"
    document.getElementById(hint_id).innerHTML = inner_html + hint_text
}

function setLenP(condition){
    setHintById(condition,"Minimum length: 8 characters.","pass-hint-length")
}

function setASCIIP(condition){
    setHintById(condition,"Use english characters and numbers.","pass-hint-ascii")
}

function setNumAndLetterP(condition){
    setHintById(condition,"At least one number and letter","pass-hint-numletter")
}

function setSimilarityP(condition){
    setHintById(condition,"Password should differ from username.","pass-hint-similarity")
}

function checkPass(password){
    if(password.length==0){
        setAllFalse()
        setAllLabels()
        return
    }
    if(re_password_triggered){
        checkPasswordMatch()
    }
    checkLen(password)
    checkASCII(password)
    checkNumAndLetter(password)
    checkSimiliarity(password)
    setAllLabels()
    configureBtn()
}


function pAppear(elem){
    if(passwordMatch)
        return
    var elem = document.getElementById(elem);
    elem.style.display = "block"
    var elem_opacity = 0;
    var id = setInterval(frame, 0.5);
    function frame() {
        if (elem_opacity>=1) {
            clearInterval(id);
        } else {
            elem_opacity=elem_opacity+0.01;
            elem.style.opacity = "" + elem_opacity
        }
    }
}

function pDissappear(elem){
    if(!passwordMatch)
        return
    var elem = document.getElementById(elem);
    var elem_opacity = 1;
    var id = setInterval(frame, 0.5);
    function frame() {
        if (elem_opacity<=0) {
            clearInterval(id);
            // document.getElementById('repeat-password-input-p').style.display = "none"
            //ar gaqreba , adgils igives ikavebs bolomde
        } else {
            elem_opacity=elem_opacity-0.01;
            elem.style.opacity = "" + elem_opacity
        }
    }
}

function hintAppear(){
    var elem = document.getElementById("pass-hint-container");
    elem.style.display = "block"
    var elem_height = elem.style.height.substring(0,elem.style.height.length-2)/1
    elem.style.opacity = elem_height/140.0 + ""
    var id = setInterval(frame, 0.5)
    function frame() {
        if (elem_height>=122) {
            clearInterval(id);
        } else {
            elem_height+=1.75
            elem.style.height = elem_height +"px"
            elem.style.opacity = elem_height/140.0 + ""
        }
    }
    //ver davakavshire porcentebit amitom fiqsirebul heightamde amyavs (122px)
}

function hintDissappear(){
    var elem = document.getElementById("pass-hint-container");
    var elem_height = elem.style.height.substring(0,elem.style.height.length-2)/1
    elem.style.opacity = elem_height/140.0 + ""
    var id = setInterval(frame, 0.5);
    function frame() {
        if (elem_height<=0) {
            elem.style.display = "none"
            clearInterval(id);
        } else {
            elem_height-=1.75;
            elem.style.height = elem_height +"px"
            elem.style.opacity = elem_height/140.0 + ""
        }
    }
}


function checkPasswordMatch(){
    var password = document.getElementById('password-input').value
    var rep_password = document.getElementById('repeat-password-input').value
    if(passwordMatch!=(password===rep_password)){
        passwordMatch = password===rep_password
        configureBtn()
        if(passwordMatch){
            pDissappear("repeat-password-input-p")
        }
        else{
            pAppear("repeat-password-input-p")
        }
    }
}

function validateEmail(){
    condition =  /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(document.getElementById('email-input').value)
    if(!condition&&(document.getElementById("email-input").value!='')){
        clearTimeout(emailTimeout)
        $('#email-input').popover('show');
        emailTimeout = setTimeout(function(){
            $('#email-input').popover('hide')
        }, 2000)
    }
    return condition
}

function checkForm() {
    var cond1 = configureBtn()
    var cond2 = checkEmptyFields()
    var cond3 = validateEmail()
    return cond1&&cond2&&cond3
}

function checkEmptyFields(){
    var cond = true
    if(checkUsernameEmpty())
        cond=false
    if(checkEmailEmpty())
        cond=false
    if(checkPassEmpty())
        cond=false
    if(checkRepPassEmpty())
        cond=false
    return cond
}

function animateWarningInput(elem,condition){
    if(!condition){
        elem.style.backgroundColor = "white"
        return
    }
    var elem_opacity = 0
    var id = setInterval(frame, 0.5)
    function frame() {
        if (elem_opacity>=0.45) {
            clearInterval(id);
        } else {
            elem_opacity = elem_opacity+0.005
            elem.style.backgroundColor = "rgba(245, 6, 78,"+elem_opacity+")"
        }
    }
}

function checkUsernameEmpty(){
    var elem = document.getElementById("username-input")
    var cond = elem.value.length < 8
    animateWarningInput(elem,cond)
    return cond
}

function checkEmailEmpty(){
    var elem = document.getElementById("email-input")
    var cond = elem.value===""
    animateWarningInput(elem,cond)
    return cond
}

function checkPassEmpty(){
    var elem = document.getElementById("password-input")
    var cond = elem.value===""
    animateWarningInput(elem,cond)
    return cond
}

function checkRepPassEmpty(){
    var elem = document.getElementById("repeat-password-input")
    var cond = elem.value===""
    animateWarningInput(elem,cond)
    return cond
}


function checkAvailable(){
    var formCondition = checkForm()
    if(!formCondition){
        return false
    }

    var processForm = true

    var usernameAvaliable = false;
    var emailAvaliable = false;


    try{
        $.ajax({url:"RegisterServlet",
            type: 'POST',
            data:{
                username: $("#username-input").val(),
                email: $("#email-input").val(),
                registerType: "ajax"
            },
            success: function(data){
                var res = data.split(" ");
                if(res[0]==="true"){
                    usernameAvaliable = true
                }
                if(res[1]==="true"){
                    emailAvaliable = true
                }
            },
            async:false
        });
    }catch(err){
        console.log(err.message)
    }

    if(!usernameAvaliable){
        processForm = false
        clearTimeout(usernameTimeout)
        $('#username-input').popover('show');
        usernameTimeout = setTimeout(function(){
            $('#username-input').popover('hide')
        }, 2000)
    }

    if(!emailAvaliable){
        processForm = false
        clearTimeout(emailTimeout)
        $('#email-input').popover('show');
        emailTimeout = setTimeout(function(){
            $('#email-input').popover('hide')
        }, 2000)
    }

    return processForm
}


window.onload = function(){
    document.getElementById("password-input").addEventListener('input', function(evt){
        checkPass(this.value)
    });
    document.getElementById("username-input").addEventListener('input', function(evt){
        checkPass(document.getElementById("password-input").value)
    });
    document.getElementById("password-input").addEventListener('focusout', function(evt){
        hintDissappear()
    });
    document.getElementById("password-input").addEventListener('focus', function(evt){
        setAllLabels()
        hintAppear()
    });
    document.getElementById("repeat-password-input").addEventListener('focusout', function(evt){
        re_password_triggered = true
        checkPasswordMatch()
    });
    document.getElementById("repeat-password-input").addEventListener('input', function(evt){
        if(re_password_triggered)
            checkPasswordMatch()
    });
}
