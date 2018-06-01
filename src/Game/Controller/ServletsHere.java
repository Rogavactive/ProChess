package Game.Controller;

public class ServletsHere {
    private static ServletsHere ourInstance = new ServletsHere();

    public static ServletsHere getInstance() {
        return ourInstance;
    }

    private ServletsHere() {
    }
}
