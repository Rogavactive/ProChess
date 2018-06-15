$(document).ready(function(){
    //write here ajax async call to server, it will look for player n2 and callback function should handle response
    console.log("everything works fine! =_= ")
});

function SearchRequest(){
    var gameType = $( "#choose-type option:selected" ).val();
    var primaryTime = $( "#main-time option:selected" ).val();
    var bonusTime = $( "#bonus-time option:selected" ).val();
    if(gameType===""||primaryTime===""||bonusTime==="")
        return
    try {
        $.ajax({
            url: "GameSearchServlet",
            type: 'POST',
            data: {
                game_type : gameType,
                time_primary : primaryTime,
                time_bonus: bonusTime
            },
            success: ajaxCallback()
        });
    } catch (err) {
        console.log(err.message)
    }
}

function ajaxCallback(data) {
    console.log(JSON.stringify(data))
}