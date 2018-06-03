var checkTimeout = null;


function checkAvailable() {
    var response = false;

    try {
        $.ajax({
            url: "LoginServlet",
            type: 'POST',
            data: {
                username: $("#username-input").val(),
                password: $("#password-input").val(),
                loginType: "ajax"

            },
            success: function (data) {
                if (data === "true") {
                    response = true
                }
            },
            async: false
        });
    } catch (err) {
        console.log(err.message)
    }

    if (!response) {
        clearTimeout(checkTimeout);
        $('#username-input').popover('show');
        checkTimeout = setTimeout(function () {
            $('#username-input').popover('hide')
        }, 2000)
    }
    return response
}

function onLoadGoogleCallback(){
    gapi.load('auth2', function() {
        auth2 = gapi.auth2.init({
            client_id: '690644503931-dtn1qj0me45ovni28qbsa12g8d6c2ccf.apps.googleusercontent.com',
            cookiepolicy: 'single_host_origin',
            scope: 'profile'
        });

        auth2.attachClickHandler(document.getElementById('googleSignIn'), {},
            function(googleUser) {
                try {
                    $.ajax({
                        url: "LoginServlet",
                        type: 'POST',
                        data: {
                            email: googleUser.getBasicProfile().getEmail(),
                            loginType: "google"
                        },
                        success: function () {
                            window.location.replace("http://localhost:8080/main.jsp")
                        },
                        async: true
                    });
                } catch (err) {
                    console.log(err.message)
                }
            }, function(error) {
                console.log('Sign-in error', error);
            }
        );
    });
}


