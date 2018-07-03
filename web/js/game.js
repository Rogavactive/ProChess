var gameWebSocket = null;
var chatWebSocket = null;

$(document).ready(function(){
    connectGame()
    connectChat()
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