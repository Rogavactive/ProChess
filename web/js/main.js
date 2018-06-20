var findGameInterval = null;

$(document).ready(function(){
    $("#link-holder").click(function () {
        $(this).select();
    });
    //write here ajax async call to server, it will look for player n2 and callback function should handle response
});

function SearchRequest(){
    var gameType = $( "#choose-type option:selected" ).val();
    var primaryTime = $( "#main-time option:selected" ).val();
    var bonusTime = $( "#bonus-time option:selected" ).val();
    if(gameType===""||primaryTime===""||bonusTime==="")
        return;
    try {
        $.ajax({
            url: "GameSearchServlet",
            type: 'POST',
            data: {
                game_type : gameType,
                time_primary : primaryTime,
                time_bonus: bonusTime
            },
            success: function(data){
                ajaxCallback(data);
            }
        });
    } catch (err) {
        console.log(err.message)
    }
    if(gameType=="random")
        createFindGameTimer();
    $('#search-btn').prop("disabled",true);
}

function createFindGameTimer() {
    clearInterval(findGameInterval);
    var time = 0;
    $( ".find-game" ).css( "visibility", "visible" );
    findGameInterval = setInterval(function () {
            time++;
            var time_str = getTimeString(time);
            $('#find-game-timer').html(time_str)
        }, 1000)
}

function getTimeString(time){
    var minutes = Math.floor(time/60);
    if(minutes<10)
        minutes = "0"+minutes;
    var seconds = time%60;
    if(seconds<10)
        seconds = "0"+seconds;
    return minutes + ":" + seconds;
}

function ajaxCallback(data) {
    var parsed_data = JSON.parse(data);
    var url = "http://localhost:8080/game.jsp?id=" + parsed_data.id;
    if(parsed_data.type===1){//0-random, 1-friendly, 2-bot
        setLink(url)
    }else{
        window.location.href = url;
    }

}

function setLink(link){
    $('#link-holder').val(link);
    $('#copy-btn').removeAttr("disabled");
}

function CopyLink() {
    var copyText = document.getElementById("link-holder");
    copyText.select();
    document.execCommand("copy");
}