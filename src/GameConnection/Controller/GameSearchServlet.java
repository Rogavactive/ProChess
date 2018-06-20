package GameConnection.Controller;

import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/GameSearchServlet")
public class GameSearchServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //search for the game, store user's session and search for the game here. if found send them callbacks.
        //input: game_type:random, friendly, bot   ,   time_primary:1,2,5,10,15     ,    time_bonus:1,2,5,10
        JSONObject response_json = new JSONObject();
        response_json.put("type", 1);
        response_json.put("id", "ad1daf423ad1sd33");//generate random non-repeatable id.
        String response_text = response_json.toString();
        response.getWriter().write(response_text);//pass JSON here.
        //output: type:(0 - random,1 - friendly,2 - bot), id:generated_match_id
    }
}
