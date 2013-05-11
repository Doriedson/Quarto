package gamer.quarto;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Paint;

public class Pink extends Sprite {

	
	public int speed = 1; // Velocidade da movimentação/animação.
	public byte direction = 0; // Direção da movimentação/animação.
	public boolean dying = false, dead = false;
	protected byte animationSpeedControl = 0;

	public Pink(int x, int y, Random random, Bitmap bmp, int bmp_rows, int bmp_columns, int tela) {
		super(bmp, bmp_rows, bmp_columns, tela);
		direction = (byte) random.nextInt(8); // Direção aleatória.
		int frame = 1 + direction*3; // Seta o frame inicial para a direção
		setAnimation(frame, frame, frame+3, ANIM_GOBACK);
		this.x = x;
		this.y = y;
	}

	@Override
	public void update() {
		animationSpeedControl++; // Esta versão do Android Game Engine não
		if(animationSpeedControl >= 6 - speed){ // suporta mudança na 
			super.update(); // velocidade da animação, então foi feito
			animationSpeedControl = 0; // esse hack para diminuir a 
		} // velocidade, que estava muito rápida.
	      
		if(!dying){ // Se não está morrendo, anda na direção.
			x += direction % 4 > 0 ? direction > 4 ? -speed : speed : 0;
			y += Math.abs((direction-2) % 4) > 0 ? direction > 6 || direction < 2 ? speed : -speed : 0;
		}
		else{ // Se está morrendo, aumenta a transparência até ficar  invisível.
			int alpha = mPaint.getAlpha();  
			if(alpha <= 25)
				dead = true;
			else
				mPaint.setAlpha(alpha-10);
		}
	}

	public void changeDirection(byte direct){ // Muda a direção e muda a  animação de acordo.
		direction = direct; 
		int frame = 1 + direction*3;
		setAnimation(frame, frame, frame+3, ANIM_GOBACK);
	}
  
	public void kill(){ // Função que será chamada quando o jogador clicar no Pink.
		dying = true;
		setAnimation(0, 0, 1, ANIM_STOP);
		mPaint = new Paint();
	}	

	public boolean isDead() {
	      return dying || dead;
	}
}