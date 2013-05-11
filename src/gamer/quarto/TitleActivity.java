package gamer.quarto;

import android.app.Activity;
import android.os.Bundle;

public class TitleActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(new TitleGameView(this));
	}
}
