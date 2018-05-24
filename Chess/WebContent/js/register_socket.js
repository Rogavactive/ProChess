function disableBtn(){
    document.getElementById("register-btn").disabled=true;
}

function enableBtn(){
    document.getElementById("register-btn").disabled=false;
}

function checkLen(password){
    var res = password.length > 7
    setLenP(res)
    return res
}

function checkASCII(password) {
    var res = /^[\x20-\x7E]*$/.test(password)
    setASCIIP(res)
    return res
}

function checkNumAndLetter(password){
    var res = /\d/.test(password) && (/[a-z]/i.test(password) || /[A-Z]/i.test(password))
    setNumAndLetterP(res)
    return res
}

function checkSimiliarity(password){
    var res = !(password==document.getElementById("username-input").value)
    setSimilarityP(res)
    return res
}

function setHintById(condition, hint_text, hint_id){
    var inner_html = ""
    if(condition)
        inner_html = "<i class=\"material-icons hint-icons hint-accept\">check</i>"
    else
        inner_html = "<i class=\"material-icons hint-icons hint-reject\">close</i>"
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
    setHintById(condition,"Pssword should differ from username.","pass-hint-similarity")
}

function checkPass(password){
    if(password.length==0){
        enableBtn()
        return
    }
    var passCounter = 0;
    if(checkLen(password)){
        passCounter++;
    }
    if(checkASCII(password)){
        passCounter++;
    }
    if(checkNumAndLetter(password)){
        passCounter++;
    }
    if(checkSimiliarity(password)){
        passCounter++;
    }
    if(passCounter==4)
        enableBtn()
    else
        disableBtn()
}

window.onload = function(){

    document.getElementById("password-input").addEventListener('input', function(evt){
        checkPass(this.value)
    });
    document.getElementById("password-input").addEventListener('focus', function(evt){
        document.getElementById("register-box-id").style.height = "500px"
        document.getElementById("pass-hint-container").style.display = "block"
        setLenP(false)
        setASCIIP(false)
        setNumAndLetterP(false)
        setSimilarityP(false)
    });
}


window.onbeforeunload = function(){
    //on-close
}