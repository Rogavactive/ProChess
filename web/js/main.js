var findGameInterval = null;
var searchWebSocket = null;

$(document).ready(function(){
    $("#link-holder").click(function () {
        $(this).select();
    });
    initWebsocket();

    //write here ajax async call to server, it will look for player n2 and callback function should handle response
});

function initWebsocket() {
    try {
        this.searchWebSocket = new WebSocket("ws://localhost:8080/gamesearch");
        this.searchWebSocket.onopen = function(event) {
            if(opponentID!==0){
                SearchRequest();
                $('#search-btn').prop("disabled",true);
            }
        }
        this.searchWebSocket.onmessage = function(event) {
            callback(JSON.parse(event.data))
        }
        this.searchWebSocket.onclose = function(event) {
            //nothing
        }
        this.searchWebSocket.onerror = function(event) {
            console.log('error: ' + JSON.stringify(event, null, 4));
        }
    } catch (exception) {
        console.error(exception);
    }
}

function SearchRequest(){
    var gameType = $( "#choose-type option:selected" ).val();
    var primaryTime = $( "#main-time option:selected" ).val();
    var bonusTime = $( "#bonus-time option:selected" ).val();
    if(opponentID===0&&(gameType===""||primaryTime===""||bonusTime===""))
        return;

    if(opponentID!==0)
        gameType="1";

    // message = '{"game_type" : "3",' +
    //     '"opponent_link" : "' + opponentLink +'",' +
    //     '"time_primary" : "' + primaryTime +'",' +
    //     '"time_bonus" : "'+bonusTime+'"}';
    var message = '{"game_type" : "' + gameType+ '",' +
            '"opponent_id" : "' + opponentID +'",' +
            '"time_primary" : "' + primaryTime +'",' +
            '"time_bonus" : "'+bonusTime+'"}';



    console.log(message);
    if (this.searchWebSocket.readyState == searchWebSocket.OPEN) {
        this.searchWebSocket.send(message);
    } else {
        console.error('webSocket is not open. readyState=' + this.searchWebSocket.readyState);
    }

    if(gameType==='0'){
        createFindGameTimer();
        $('#search-btn').prop("disabled",true);
    }
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

function callback(data) {
    console.log(data);
    if(data.type===-1){
        //You are already in game!
        console.log("ingame you bastard!")
        return;
    }
    if(data.type===3){//0-random, 1-friendly, 2-bot, 3-friendly link
        createFindGameTimer();
        $('#search-btn').prop("disabled",true);
        setLink(data.link)
    }else {
        var url = "http://localhost:8080/game.jsp?id=" + data.id;
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