var gameWebSocket = null;
var chatWebSocket = null;
var board=null;
$(document).ready(function(){
    connectGame();
    connectChat();

    board=$('#chess_board');
    started();
});

//GAME SOCKET

function connectGame() {
    try {
        this.gameWebSocket = new WebSocket("ws://localhost:8080/game");
        this.gameWebSocket.onopen = function(event) {
            console.log('onopen::' + JSON.stringify(event, null, 4));
        }
        this.gameWebSocket.onmessage = function(event) {
            var msg = event.data;
            console.log('onmessage::' + JSON.stringify(msg, null, 4));

        }
        this.gameWebSocket.onclose = function(event) {
            console.log('onclose::' + JSON.stringify(event, null, 4));
        }
        this.gameWebSocket.onerror = function(event) {
            console.log('onerror::' + JSON.stringify(event, null, 4));
        }
    } catch (exception) {
        console.error(exception);
    }
}
var Blacks_Bottom_Positions = {
    "00": "B_etli", "01": "B_mxedari", "02": "B_ku", "03": "B_mepe", "04": "B_dedopali", "05": "B_ku", "06": "B_mxedari", "07": "B_etli",
    "10": "B_paiki", "11": "B_paiki", "12": "B_paiki", "13": "B_paiki", "14": "B_paiki", "15": "B_paiki", "16": "B_paiki", "17": "B_paiki",
    "60": "W_paiki", "61": "W_paiki", "62": "W_paiki", "63": "W_paiki", "64": "W_paiki", "65": "W_paiki", "66": "W_paiki", "67": "W_paiki",
    "70": "W_etli", "71": "W_mxedari", "72": "W_ku", "73": "W_mepe", "74": "W_dedopali", "75": "W_ku", "76": "W_mxedari", "77": "W_etli",
}

var Pieces_Ascii = {
    "mepe": ["King", "&#9813;", "&#9819;"],
    "dedopali": ["Queen", "&#9812;", "&#9818;"],
    "ku": ["Bishop", "&#9815;", "&#9821;"],
    "mxedari": ["Knight", "&#9816;", "&#9822;"],
    "etli": ["Rook", "&#9814;", "&#9820;"],
    "paiki": ["Pawn", "&#9817;", "&#9823;"]
}

function fill_Chessboard(BottomColor){
    for(var position in Blacks_Bottom_Positions ){
        // Split the string up to find out color and piece name of the current position
        var piece = Blacks_Bottom_Positions[position].substring(2);
        var color = Blacks_Bottom_Positions[position][0];
        var ascii;

        if (color==BottomColor){
            ascii=Pieces_Ascii[piece][1];
        } else {
            ascii=Pieces_Ascii[piece][2];
        }
        document.getElementById(position).innerHTML = "<a href='#'>" +ascii + "</a>";

    }
}
function clearChessboard(){
    for (var i=0;i<8;i++){
        for (var j=0;j<8;j++){
            document.getElementById(i+''+j).innerHTML = "";
        }
    }
}
var lastCell=null;
function changeCellColor(cell,color){
    document.getElementById(cell).style.color = color;
}
function replacePieces(cell1,cell2){
   var saveHtml=  document.getElementById(cell1).innerHTML;
   console.log(cell1+' '+cell2);
    document.getElementById(cell1).innerHTML=document.getElementById(cell2).innerHTML;
    document.getElementById(cell2).innerHTML=saveHtml;
}
function clickOnCell(cell){
    if (lastCell==null) {
        lastCell = cell;
        changeCellColor(cell,'yellow');
    } else {
        replacePieces(lastCell,cell);
        changeCellColor(lastCell,'transparent');
        lastCell=null;
    }

  //  document.getElementById(cell).parentNode.style.backgroundColor='red';
  //  document.getElementById(cell).parentNode.parentNode.style.backgroundColor='red';
  //  document.getElementById(cell).parentNode.parentNode.parentNode.style.backgroundColor='red';
}

function started(){
    fill_Chessboard('B');
    $(document).click(function(event) {
        var text = event.target.parentNode.id;
        if (text.length!=2) text=event.target.id;
        console.log(text);
        clickOnCell(text);

    });
    //clearChessboard();
    // document.getElementById('00').innerHTML = "<a href='#'>" +"&#9820;" + "</a>";
    //  console.log("2");
}
function getPieceMoves(){

}
function pieceMoved(){

}

function getGameStatus() {
    return this.gameWebSocket.readyState;
}

function sendToGame(message){

    if (getGameStatus() == gameWebSocket.OPEN) {
        this.gameWebSocket.send(message);
    } else {
        console.error('webSocket is not open. readyState=' + getGameStatus());
    }

}

function disconnectFromGame() {

    if (getGameStatus() == gameWebSocket.OPEN) {
        this.gameWebSocket.close();
    } else {
        console.error('webSocket is not open. readyState=' + getGameStatus());
    }

}

//CHAT SOCKET

function connectChat(){
    try {
        this.chatWebSocket = new WebSocket("ws://localhost:8080/gamechat");
        this.chatWebSocket.onmessage = function(event) {
            var chatserverdata = JSON.parse(event.data);
            $('#chatBox').append("<p class='chat-username-p'>"+chatserverdata.username+"</p><p class='chat-message-p'>"+":"+ chatserverdata.message+"</p><br/>")
            $('#chatBox').scrollTop($('#chatBox')[0].scrollHeight);
            // console.log('onmessage::' + JSON.stringify(chatserverdata.message, null, 4));
        }
        this.chatWebSocket.onopen = function(event) {
            // console.log('onopen::' + JSON.stringify(event, null, 4));
        }
        this.chatWebSocket.onclose = function(event) {
            console.log('onclose::' + JSON.stringify(event, null, 4));
        }
        this.chatWebSocket.onerror = function(event) {
            console.log('onerror::' + JSON.stringify(event, null, 4));
        }
    } catch (exception) {
        console.error(exception);
    }
}

function getChatStatus() {
    return this.chatWebSocket.readyState;
}

function sendToChat(message){

    if (getChatStatus() == gameWebSocket.OPEN) {
        this.chatWebSocket.send(message);
    } else {
        console.error('webSocket is not open. readyState=' + getChatStatus());
    }

}

function disconnectFromGame() {

    if (getChatStatus() == gameWebSocket.OPEN) {
        this.chatWebSocket.close();
    } else {
        console.error('webSocket is not open. readyState=' + getChatStatus());
    }

}

function chatBtnClick() {
    sendToChat($('#chatMessage').val());
    $('#chatMessage').val("")
}

function makeMove() {

}