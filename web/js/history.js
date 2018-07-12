window.onclose = function () {
    sendAjax("end_game");
};


var Pieces_Ascii = {
    "mepe": ["King", "&#9813;", "&#9819;"],
    "dedopali": ["Queen", "&#9812;", "&#9818;"],
    "ku": ["Bishop", "&#9815;", "&#9821;"],
    "mxedari": ["Knight", "&#9816;", "&#9822;"],
    "etli": ["Rook", "&#9814;", "&#9820;"],
    "paiki": ["Pawn", "&#9817;", "&#9823;"]
};

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

var Player;
var markColor='yellow';
var validMoveColor='green';
var MovesState='';
var gameStarted=false;

function sendNext() {
    console.log("its next")
    sendAjax("next")
}

function sendPrev() {
    console.log("its prev")
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
    // if(data.type==="board_state"){
    //     BoardStateChanged(data)
    // }else if(data.type==="winner_state"){
    //     var winner = false;
    //     if(data.winner==="success"){
    //         winner = true;
    //     }
    //     setWInnerLabel(winner);
    //     MovesState = '';
    // }
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
    // Player=msg.player;
    // console.log("player: "+Player);
    var board = msg.board;
    console.log(board);
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

    MovesState="";
    // var movesStateToParse = msg.moves;
    // console.log(movesStateToParse);
    // for (var i = 0; i < movesStateToParse.length; i++){
    //     if (Player==='B') MovesState+= 7-movesStateToParse[i];
    //     else MovesState+=movesStateToParse[i];
    // }

    console.log(MovesState);

    if (!gameStarted) {
        startGame();
        gameStarted=true;
    }
}

function placePieceInCell(cell,piece){
    document.getElementById(cell).innerHTML = "<a href='#'>" +piece + "</a>";

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

// function movePiece(cell1,cell2){
//     var saveHtml=  document.getElementById(cell1).innerHTML;
//     console.log(cell1+' '+cell2);
//     document.getElementById(cell1).innerHTML=document.getElementById(cell2).innerHTML;
//     document.getElementById(cell2).innerHTML=saveHtml;
//     //send to socket
//     response=cell1+cell2;
//     if (Player=='B'){
//         var response=(7-cell1[0])+''+(7-cell1[1])+''+(7-cell2[0])+''+(7-cell2[1]);
//     }
//     sendToGame(response);
//     console.log(response);
//     MovesState='';
// }

// function clickOnCell(cell){
//     if (lastCell==null) {
//         var validMoves=getValidMovesForPiece(cell);
//
//         if (validMoves.length==0) return;
//         for (var i=0;i<validMoves.length;i++) {
//             changeCellColor(validMoves[i], validMoveColor)
//             //  console.log(validMoves[i]);
//         }
//         lastCell = cell;
//         changeCellColor(cell,markColor);
//     } else {
//         var validMoves=getValidMovesForPiece(lastCell);
//         for (var i=0;i<validMoves.length;i++)
//             if (validMoves[i]==cell) {
//                 movePiece(lastCell, cell);
//                 resetCellColors();
//                 lastCell = null;
//                 return;
//             }
//         resetCellColors();
//         lastCell=null;
//         clickOnCell(cell);
//     }

    //  document.getElementById(cell).parentNode.style.backgroundColor='red';
    //  document.getElementById(cell).parentNode.parentNode.style.backgroundColor='red';
    //  document.getElementById(cell).parentNode.parentNode.parentNode.style.backgroundColor='red';
// }

// function startGame(){
//
//     $(document).click(function(event) {
//         var text = event.target.parentNode.id;
//         if (text.length!=2) text=event.target.id;
//         // console.log(text);
//         clickOnCell(text);
//
//     });
// }
//
// function sendToGame(message){
//
//     console.log("message: " + message);
//
//     try {
//         $.ajax({
//             url: "PuzzleServlet",
//             type: 'POST',
//             data: {
//                 type: "make_move",
//                 move: message
//
//             },
//             success: function (data) {
//                 callback(data)
//             }
//         });
//     } catch (err) {
//         console.log(err.message)
//     }
//     setTimeout(function () {
//         askForCompMove();
//     }, 1000);
//
// }

