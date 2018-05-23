function disableBtn(){
    document.getElementById("register-btn").disabled=true;
}

function enableBtn(){
    document.getElementById("register-btn").disabled=false;
}

function checkLen(password){
    return password.length>7
}

function checkASCII(password){
    return /^[\x20-\x7E]*$/.test(password)
}

function checkNumAndLetter(password){
    return /\d/.test(password) && (/[a-z]/i.test(password) || /[A-Z]/i.test(password))
}

function checkSimiliarity(password){
    return !(password==document.getElementById("username-input").value)
}

function checkPass(password){
    //this will change DOM. a little board that indicates whats wrong.
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
    // document.getElementById("register-btn").onclick = function(){
    //     document.getElementById("test-paragraph").innerHTML = "Button clicked!"
    // }
}


window.onbeforeunload = function(){
    //on-close
}