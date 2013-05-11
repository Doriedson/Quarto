package gamer.quarto;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Background extends Sprite {

	private Rect src, dest;
	private Bitmap bmp;
	private int raio=0, x=0, y=0, stageWidth, stageHeight;
	private double angulo=0;
	
	//private Bitmap mBitmap; // O Bitmap final (background montado).

	public Background(Bitmap bmp, int stageWidth, int stageHeight, int tela) {

		super(bmp, stageWidth, stageHeight, tela);

		//mBitmap = Bitmap.createBitmap(stageWidth, stageHeight, Bitmap.Config.ARGB_8888);
		//Canvas canvas = new Canvas(mBitmap); // Usa-se um canvas para desenhar no Bitmap.
		this.stageWidth=stageWidth;
		this.stageHeight=stageHeight;

		raio=(int)(bmp.getHeight()-stageHeight)/2;
		
		src = new Rect(x, y, stageWidth + x, stageHeight + y);
		dest = new Rect(0,0,stageWidth, stageHeight);
		this.bmp=bmp;
		//canvas.drawBitmap(bmp, source, destiny, null);
	}

	public void onDraw(Canvas canvas) {
		//canvas.drawBitmap(mBitmap, 0, 0, null); // Substitui o onDraw original para desenhar o Bitmap montado.
		canvas.drawBitmap(bmp, src, dest, null);
	}
	
	public void update(){
		angulo+=0.25;
		angulo=angulo % 360.0;
		y=(int) (Math.sin((angulo/180.0)*Math.PI)*raio)+raio;
		x=(int) (Math.cos((angulo/180.0)*Math.PI)*raio)+(bmp.getWidth()/2);

		src.right= stageWidth+ (src.left=x);
		src.bottom= stageHeight+ (src.top=y);
		//System.out.println(x + "," + y);
	}
	
}

