function disableBtn(){
    document.getElementById("register-btn").disabled=true;
}

function enableBtn(){
    document.getElementById("register-btn").disabled=false;
}

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
    asciiCheckCond = /^[\x20-\x7E]*$/.test(password)
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
        enableBtn()
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
    if(similarityCheckCond&&asciiCheckCond&&numAndLetterCheckCond&&lenCheckCond&&passwordMatch)
        enableBtn()
    else
        disableBtn()
}


function passPappear(){
    if(passwordMatch)
        return
    var elem = document.getElementById("repeat-password-input-p");  
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

function passPdissappear(){
    if(!passwordMatch)
        return
    var elem = document.getElementById("repeat-password-input-p");   
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
    var id = setInterval(frame, 0.5)
    function frame() {
        if (elem_height>=122) {
            clearInterval(id);
        } else {
            elem_height+=2
            elem.style.height = elem_height +"px"
        }
    }
    //ver davakavshire porcentebit amitom fiqsirebul heightamde amyavs (122px)
}

function hintDissappear(){
    var elem = document.getElementById("pass-hint-container");
    var elem_height = elem.style.height.substring(0,elem.style.height.length-2)/1
    var id = setInterval(frame, 0.5);
    function frame() {
        if (elem_height<=0) {
            elem.style.display = "none"
            clearInterval(id);
        } else {
            elem_height-=2; 
            elem.style.height = elem_height +"px"
        }
    }
}


function checkPasswordMatch(){
    var password = document.getElementById('password-input').value
    var rep_password = document.getElementById('repeat-password-input').value
    if(passwordMatch!=(password===rep_password)){
        passwordMatch = password===rep_password
        if(passwordMatch){
            enableBtn()
            passPdissappear()
        }
        else{
            disableBtn()
            passPappear()
        }
    }
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
