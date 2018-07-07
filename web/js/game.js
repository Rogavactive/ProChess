
var Pieces_Ascii = {
    "mepe": ["King", "&#9813;", "&#9819;"],
    "dedopali": ["Queen", "&#9812;", "&#9818;"],
    "ku": ["Bishop", "&#9815;", "&#9821;"],
    "mxedari": ["Knight", "&#9816;", "&#9822;"],
    "etli": ["Rook", "&#9814;", "&#9820;"],
    "paiki": ["Pawn", "&#9817;", "&#9823;"]
}

var socked_Piece = {
    "WQ":"&#9813;",
    "BQ":"&#9819;",
    "WK":"&#9812",
    "BK":"&#9818;",
    "WB":"&#9815;",
    "BB":"&#9821;",
    "WN":"&#9816;",
    "BN":"&#9822;",
    "WP":"&#9817;",
    "BP":"&#9823;",
    "WR":"&#9814;",
    "BR":"&#9820;",
    "00":"",
}

var Player;
var markColor='yellow';
var validMoveColor='green';
var MovesState='';
var gameStarted=false;

var gameWebSocket = null;
var chatWebSocket = null;

$(document).ready(function(){
    connectGame();
    connectChat();
    //BoardStateChanged("B60506040615161416252624263536343645464446555654566566646675767477150715276557657");
});

//GAME SOCKET

function connectGame() {
    try {
        this.gameWebSocket = new WebSocket("ws://localhost:8080/game");
        this.gameWebSocket.onopen = function(event) {
            var msg = event.data;
            console.log( msg);
            //  BoardStateChanged(msg);
        }

        this.gameWebSocket.onmessage = function(event) {
            var msg = event.data;
            console.log( msg)
            // console.log('onmessage::' + JSON.stringify(msg, null, 4));
            BoardStateChanged(msg);
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

function getValidMovesForPiece(pos){
    var moves=[];

    for (var i=0;i<MovesState.length;i+=4){
        var x=MovesState.substring(i,i+2);
        var y=MovesState.substring(i+2,i+4);
        // console.log(x+' '+y);
        if (x==pos) moves.push(y);

    }
    return moves;
}


function BoardStateChanged(msg){
    Player=msg[128];
    var ind=0;
    for (var i=0;i<8;i++) {
        for (var j=0;j<8;j++) {
            var piece = msg[ind] + msg[ind + 1];
            var cell=i+''+j;
            if (Player=='B') cell=(7-i)+''+(7-j);
            //   console.log(cell+" "+piece);
            placePieceInCell(cell,socked_Piece[piece]);
            ind+=2;
        }
    }

    MovesState="";
    console.log(Player);
    for (var i = 129; i < msg.length; i++){
        if (Player=='B') MovesState+= 7-msg[i];
        else MovesState+=msg[i];
    }

     console.log(MovesState);

    if (!gameStarted) {
        startGame();
        gameStarted=true;
    }
}

function placePieceInCell(cell,piece){
    document.getElementById(cell).innerHTML = "<a href='#'>" +piece + "</a>";

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
    // console.log(2+document.getElementById(cell).style.backgroundColor);
    document.getElementById(cell).style.backgroundColor = color;
}
function resetCellColors(){
    for (var i=0;i<8;i++)
        for (var j=0;j<8;j++)
            changeCellColor(i+''+j,'');
}
function movePiece(cell1,cell2){
    var saveHtml=  document.getElementById(cell1).innerHTML;
    console.log(cell1+' '+cell2);
    document.getElementById(cell1).innerHTML=document.getElementById(cell2).innerHTML;
    document.getElementById(cell2).innerHTML=saveHtml;
    //send to socket
    response=cell1+cell2;
    if (Player=='B'){
        var response=(7-cell1[0])+''+(7-cell1[1])+''+(7-cell2[0])+''+(7-cell2[1]);
    }
    this.gameWebSocket.send(response);
    console.log(response);
    MovesState='';
}
function clickOnCell(cell){
    if (lastCell==null) {
        var validMoves=getValidMovesForPiece(cell);

        if (validMoves.length==0) return;
        for (var i=0;i<validMoves.length;i++) {
            changeCellColor(validMoves[i], validMoveColor)
            //  console.log(validMoves[i]);
        }
        lastCell = cell;
        changeCellColor(cell,markColor);
    } else {
        var validMoves=getValidMovesForPiece(lastCell);
        for (var i=0;i<validMoves.length;i++)
            if (validMoves[i]==cell) {
                movePiece(lastCell, cell);
                resetCellColors();
                lastCell = null;
                return;
            }
        resetCellColors();
        lastCell=null;
        clickOnCell(cell);
    }

    //  document.getElementById(cell).parentNode.style.backgroundColor='red';
    //  document.getElementById(cell).parentNode.parentNode.style.backgroundColor='red';
    //  document.getElementById(cell).parentNode.parentNode.parentNode.style.backgroundColor='red';
}

function startGame(){

    $(document).click(function(event) {
        var text = event.target.parentNode.id;
        if (text.length!=2) text=event.target.id;
       // console.log(text);
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