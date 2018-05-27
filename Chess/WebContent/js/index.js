var checkTimeout = null

window.onload = function(){
    document.getElementById('login-btn').onclick = function(){
        if(checkAvailable()){
            document.getElementById('LoginForm').submit()
        }
    }
}

function checkAvailable(){
    // var oReq = new XMLHttpRequest();
    // oReq.addEventListener("load", function(){
    //     console.log(this.responseText)
    // })
    // oReq.open("POST","LoginServlet")
    // console.log("modis")
    // oReq.send()


    var response = false

    if(!response){
        processForm = false
        clearTimeout(checkTimeout)
        $('#username-input').popover('show');
        checkTimeout = setTimeout(function(){
            $('#username-input').popover('hide')
        }, 2000)
    }

    return response
}


