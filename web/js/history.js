window.onclose = function () {
    sendAjax("end_game");
};
var intToACII = {
    0 : "a",
    1 : "b",
    2 : "c",
    3 : "d",
    4 : "e",
    5 : "f",
    6 : "g",
    7 : "h",
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
};


function sendNext() {
    console.log("its next");
    sendAjax("next")
}

function sendPrev() {
    console.log("its prev");
    sendAjax("prev")
}

function sendAjax(message) {
    try {
        $.ajax({
            url: "HistoryServlet",
            type: 'POST',
            data: {
                msg: message
            },
            success: function (data) {
                callback(data)
            }
        });
    } catch (err) {
        console.log(err.message)
    }
}

$(document).ready(function(){
    try {
        $.ajax({
            url: "HistoryServlet",
            type: 'POST',
            data: {
                msg: "first",
                id: game_id
            },
            success: function (data) {
                callback(data)
            }
        });
    } catch (err) {
        console.log(err.message)
    }
});

//GAME SOCKET

function callback(data){
    console.log(data);
    data = JSON.parse(data);
    console.log(data);
    BoardStateChanged(data);
}

function BoardStateChanged(msg){
    var board = msg.board;
    console.log(board);
    var ind=0;
    for (var i=0;i<8;i++) {
        for (var j=0;j<8;j++) {
            var piece = board[ind] + board[ind + 1];
            var cell=i+''+j;
            //   console.log(cell+" "+piece);
            placePieceInCell(cell,socked_Piece[piece]);
            ind+=2;
        }
    }

    var currMove = msg.move;
    if(currMove!==""){
        var moveStr = "" + intToACII[currMove[1]] + (1 +parseInt(currMove[0])) + intToACII[currMove[3]] + (1 +parseInt(currMove[2]));
        console.log(moveStr)
    }
    var bestMove = msg.best_move;
    if(bestMove===""){
        console.log("best_move_gadmoeca")
    }
}

function placePieceInCell(cell,piece){
    document.getElementById(cell).innerHTML = "<a href='#'>" +piece + "</a>";

}
