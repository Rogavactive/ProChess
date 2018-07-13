
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

var MovesState='';
var gameStarted=false;

var gameWebSocket = null;
var chatWebSocket = null;
var myName = "";
var oppName = "";
var myTime = 900;
var oppTime = 900;
var currentPlayer = "";
var countDown = null;
$(document).ready(function(){
    connectGame();
    connectChat();
    //BoardStateChanged("B60506040615161416252624263536343645464446555654566566646675767477150715276557657");
});
function startCountDown(){
    clearInterval(countDown);
    $('#myTime').html(getTimeStr(myTime));
    $('#oppTime').html(getTimeStr(oppTime));
    if(currentPlayer===Player){
        countDown = setInterval(function () {
            myTime--;
            if(myTime==0)
                clearInterval(countDown);
            var timeStr = getTimeStr(myTime);
            $('#myTime').html(timeStr)
        },1000);
    }else{
        countDown = setInterval(function () {
            oppTime--;
            if(oppTime==0)
                clearInterval(countDown);
            var timeStr = getTimeStr(oppTime);
            $('#oppTime').html(timeStr)
        },1000);
    }
}

function getTimeStr(time) {
    var minutes = Math.floor(time/60);
    if(minutes<10)
        minutes = "0"+minutes;
    var seconds = time%60;
    if(seconds<10)
        seconds = "0"+seconds;
    return minutes + ":" + seconds;
}
//GAME SOCKET
var drawIsInRequest;
var undoIsInRequest;
var resignIsInRequest;

function connectGame() {
    try {
        this.gameWebSocket = new WebSocket("ws://localhost:8080/game");
        this.gameWebSocket.onopen = function(event) {
            var msg = event.data;
            // console.log( msg);
            //  BoardStateChanged(msg);
        }

        this.gameWebSocket.onmessage = function(event) {
            var gamemessage = JSON.parse(event.data);
            console.log( gamemessage);
            // console.log('onmessage::' + JSON.stringify(msg, null, 4));
            if(gamemessage.type==="board_state")
                BoardStateChanged(gamemessage);
            if(gamemessage.type==="endgame")
                endGame(gamemessage.status);
            if(gamemessage.type==="error")
                reportError(gamemessage.message)
            if(gamemessage.type==="drawRequested") {

                drawIsInRequest=true;
                showYesNoButtons();
            }
            if(gamemessage.type==="undoRequested") {
                undoIsInRequest=true;
                showYesNoButtons();
            }
            if(gamemessage.type==="resignRequested") {

            }

            if(gamemessage.type==="drawAccepted") {
                opponentAccepted();
            }
            if(gamemessage.type==="drawDeclined") {
                opponentDeclined();
            }

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

function endGame(status) {//status = "You win" or "You lose" or "Draw"
    clearInterval(countDown);
    $('#chess_board').remove();
    $('#undo').attr('disabled','disabled');
    $('#draw').attr('disabled','disabled');
    $('#resign').attr('disabled','disabled');
    $('#chess-board-container').append('<p class=\"animated-text\">'+status+'</p>');
    console.log("endgame: " + status);
}

function reportError(err) {
    if(err==="no_game_id"){
        console.log("You are not registered in a game.");
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

var board;
function writeNames() {
    $('#myName').html(myName);
    $('#oppName').html(oppName);
}
function BoardStateChanged(msg){
    currentPlayer = msg.currentPlayer;
    Player=msg.player;
    myTime = msg.myTime;
    oppTime = msg.oppTime;
    oppName = msg.oppName;
    myName = msg.myName;
    writeNames();
    startCountDown();
    console.log("player: "+Player);
    board = msg.board;
    console.log("Board: "+board);
    var ind=0;
    for (var i=0;i<8;i++) {
        for (var j=0;j<8;j++) {
            var piece = board[ind] + board[ind + 1];
            var cell=i+''+j;
            if (Player=='B') cell=(7-i)+''+(7-j);
            //   console.log(cell+" "+piece);
            placePieceInCell(cell,socked_Piece[piece]);
            ind+=2;
        }
    }

    if(Player===currentPlayer){
        $('#undo').prop('disabled', true);
    }else{
        $('#undo').prop('disabled', false);
    }

    MovesState="";
    var movesStateToParse = msg.moves;
    console.log(movesStateToParse);
    for (var i = 0; i < movesStateToParse.length; i++){
        if (Player==='B') MovesState+= 7-movesStateToParse[i];
        else MovesState+=movesStateToParse[i];
    }

     console.log(MovesState);

    if (!gameStarted) {
        startGame();
        gameStarted=true;
    }
}
function getPieceFromCell(cell2){
    var ind=0;
    for (var i=0;i<8;i++) {
        for (var j=0;j<8;j++) {
            var piece = board[ind] + board[ind + 1];
            var cell=i+''+j;
            if (Player=='B') cell=(7-i)+''+(7-j);
            if ( cell==cell2) return  piece ;

            ind+=2;
        }
    }
}
function placePieceInCell(cell,piece){
    document.getElementById(cell).innerHTML = "<a href='#'>" +piece + "</a>";

}
function clearChessboard(){
    for (var i=0;i<8;i++){
        for (var j=0;j<8;j++){
            document.getElementById(i+''+j).innerHTML = "";
            document.getElementById(cell).innerHTML ="";
        }
    }
}
var lastCell=null;
function changePossibleMoveColors(cell) {
    // console.log(2+document.getElementById(cell).style.backgroundColor);
    //document.getElementById(cell).style.color='yellow';
    console.log(getPieceFromCell(cell));
    if (getPieceFromCell(cell)=='00'){
        document.getElementById(cell).innerHTML = "<a href='#'    style=\"color:green\">" + '&#x25C9;' + "</a> ";

    }
    else {
        document.getElementById(cell).style.backgroundColor="orange";

    }


}
    function changeMarkCellColor(cell){
        // console.log(2+document.getElementById(cell).style.backgroundColor);
        //document.getElementById(cell).style.color='yellow';
        document.getElementById(cell).style.backgroundColor="yellow";

    }
function resetCellColors(){
    for (var i=0;i<8;i++)
        for (var j=0;j<8;j++) {
        var cell=i + '' + j;
         document.getElementById(cell).style.backgroundColor='';
            document.getElementById(cell).style.opacity="1";
     }
    var ind=0;
    for (var i=0;i<8;i++) {
        for (var j=0;j<8;j++) {
            var piece = board[ind] + board[ind + 1];
            var cell=i+''+j;
            if (Player=='B') cell=(7-i)+''+(7-j);
            //   console.log(cell+" "+piece);
            placePieceInCell(cell,socked_Piece[piece]);
            ind+=2;
        }
    }
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
            changePossibleMoveColors(validMoves[i])
            //  console.log(validMoves[i]);
        }
        lastCell = cell;
        changeMarkCellColor(cell );
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
//GameOfferButtons
function showYesNoButtons() {

    var x=document.getElementById("buttons-container-yes-no");
    x.style.display="grid";
    var x=document.getElementById("buttons-container");
    x.style.display="none";
}
function showOfferButtons() {
    var x=document.getElementById("buttons-container-yes-no");
    x.style.display="none";
    var x=document.getElementById("buttons-container");
    x.style.display="grid";
}
function requestUndo(){
    this.gameWebSocket.send("undoRequested");
    sendToChat(Player.name+ " requested undo. You can Accept or Decline offer.");
    document.getElementById("undo").disabled=true;
}
function requestDraw(){
     this.gameWebSocket.send("drawRequested");
    sendToChat(Player.name+ " requested draw. You can Accept or Decline offer.");
    document.getElementById("draw").disabled=true;
}
function resign(){
    this.gameWebSocket.send("resignRequested");
    sendToChat(Player.name+ " Resigned");
}

function clickAccept(){
    if (undoIsInRequest)
        this.gameWebSocket.send("undoAccepted");
    else
        this.gameWebSocket.send("drawAccepted");
    showOfferButtons();

}
function clickDecline(){
    if (undoIsInRequest)
        this.gameWebSocket.send("undoDeclined");
    else
        this.gameWebSocket.send("drawDeclined");
    showOfferButtons();
}

function opponentAccepted(){
    console.log("offer accepted");

    if (undoIsInRequest==true)
    document.getElementById("undo").disabled=false;
    if (drawIsInRequest==true)
        document.getElementById("draw").disabled=false;
    undoIsInRequest=false;
    drawIsInRequest=false;
}
function opponentDeclined(){
    console.log("offer declined");
    undoIsInRequest=false;
    drawIsInRequest=false;
}
//CHAT SOCKET

function connectChat(){
    try {
        this.chatWebSocket = new WebSocket("ws://localhost:8080/gamechat/"+game_id);
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