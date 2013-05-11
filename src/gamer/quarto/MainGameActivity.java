package gamer.quarto;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class MainGameActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent=getIntent();
        
        setContentView(new MainGameView(this,intent.getBooleanExtra("servidor",false)) ); //R.layout.activity_truco);
    }

}