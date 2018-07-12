import sys
import chess
import chess.uci

engine = chess.uci.popen_engine("/home/paranoid/Documents/Bot/stockfish-9-linux/Linux/stockfish-9-64")
board = chess.Board()

fileName = str(sys.argv[1] + ".txt")

for i in range(2, len(sys.argv)):
    board.push(chess.Move.from_uci(str(sys.argv[i])))

engine.position(board)
fw = open(fileName, "w")
fw.write(str(engine.go(movetime = 2000)))