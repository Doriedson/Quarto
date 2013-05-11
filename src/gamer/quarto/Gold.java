package gamer.quarto;

import java.util.Random;

import android.graphics.Bitmap;

public class Gold extends Sprite {

	
	public int speedX, speedY;

public Gold(int x, int y, Random random, Bitmap bmp, int bmp_rows, int bmp_columns, int tela) {
	super(bmp, bmp_rows, bmp_columns, tela);
	setAnimation(ANIM_GO); // Seta a animação apenas como "ida" (ciclíca).
	speedX = random.nextInt(7) - 3; //Velocidade horizontal de -3 a 3.
	speedY = random.nextInt(7) - 3; //Velocidade vertical de -3 a 3.
	this.x = x;
	this.y = y;
	}

	 @Override
	 public void update() { // Anda.
	 super.update();
	 x += speedX;
	  y += speedY;
	 }	
}