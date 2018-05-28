var checkTimeout = null


function checkAvailable(){
    var response = false

    try{
	    $.ajax({url:"LoginServlet",
	    	type: 'POST',
	    	data:{
				username: $("#username-input").val(),
				password: $("#password-input").val(),
				loginType: "ajax"
				
			},
			success: function(data){
				if(data=="true"){
					response=true
				}
			},
			async:false
	    });
    }catch(err){
    	console.log(err.message)
    }

    if(!response){
        clearTimeout(checkTimeout)
        $('#username-input').popover('show');
        checkTimeout = setTimeout(function(){
            $('#username-input').popover('hide')
        }, 2000)
    }
    return response
}


