package gamer.quarto;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ServerGameActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ServerGameView(this) ); //R.layout.activity_truco);
    }

}
