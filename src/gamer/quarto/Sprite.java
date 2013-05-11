package gamer.quarto;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Sprite {
	public static final int ANIM_STOP = 0; //Constantes que definem a animação 
	public static final int ANIM_GO = 1; 
	public static final int ANIM_GOBACK = 2; 
	    
	protected int animation = ANIM_GOBACK; //Atual tipo de movimentação 
	    
	public int x = 0, y = 0; //Posição da Sprite 
	private Bitmap mBitmap; //Imagem 
	public boolean visible = true; //Visivel 
	
	private final int BMP_ROWS; //Quantidade de linhas da imagem 
	private final int BMP_COLUMNS; //Quantidade de colunas da imagem 
	
	private int currentFrame = 0; //Atual frame da animação 
	public int width,height; //Largura e altura de um frame (não do bitmap todo) 
	private int firstFrame = 0, lastFrame = 1; //Primeiro e ultimo frame da animação 
	
	private boolean animationControl = false; //Flag de controle da animação (no caso de ANIM_GOBACK) 
	public Paint mPaint; //Paint usado para desenho

	//informa qual tela o Sprite será desenhado
	private int tela=0;
	
	public Sprite(Bitmap bmp, int bmp_rows, int bmp_columns, int tela) { 
	    this.mBitmap = bmp; 
	    this.BMP_ROWS = bmp_rows; 
	    this.BMP_COLUMNS = bmp_columns; 
	    this.width = bmp.getWidth() / BMP_COLUMNS; 
	    this.height = bmp.getHeight() / BMP_ROWS; 
	    this.tela = tela;

	    System.out.println("width bmp -> " + bmp.getWidth() + "/" + BMP_COLUMNS);

	    lastFrame = BMP_COLUMNS*BMP_ROWS; 
	    if(getFrameCount() == 1) 
	            animation = ANIM_STOP; 
	} 		
	
	public void update() { 
	    switch (animation) { 
	            case ANIM_GO: 
	                    currentFrame = ((currentFrame+1-firstFrame) % (lastFrame-firstFrame)) + firstFrame; 
	                    break; 
	            case ANIM_GOBACK: 
	                    if(currentFrame+1 == lastFrame) 
	                            animationControl = true; 
	                    else if(currentFrame == firstFrame) 
	                            animationControl = false; 
	                    currentFrame = currentFrame+(animationControl ? -1 : 1); 
	                    break; 
	            } 
	}
	
	public void onDraw(Canvas canvas) { 
	    if(visible && tela==MainGameView.camera){
	            int srcX = (currentFrame % BMP_COLUMNS) * width; 
	            int srcY = (currentFrame / BMP_COLUMNS) * height; 
	            canvas.drawBitmap(mBitmap, 
	                            new Rect(srcX, srcY, srcX + width, srcY + height), 
	                            new Rect(x, y, x + width, y + height), 
	                            mPaint); 
//	            System.out.println(x + "x" + y + "x" + width + "x" + height);
	    } 
	}
	
	public void setFirstFrame(int frame){ 
	    firstFrame = frame; 
	    if(firstFrame >= lastFrame){ 
	            lastFrame = firstFrame + 1; 
	            currentFrame = firstFrame; 
	            animationControl = false; 
	    }else 
	    if(currentFrame < firstFrame){ 
	            currentFrame = firstFrame; 
	            animationControl = false; 
	    } 
	} 
	
	public void setLastFrame(int frame){ 
	    lastFrame = frame; 
	    if(lastFrame <= firstFrame){ 
	            firstFrame = lastFrame - 1; 
	            currentFrame = firstFrame; 
	            animationControl = false; 
	    }else 
	    if(currentFrame > frame){ 
	            currentFrame = firstFrame; 
	            animationControl = false; 
	    } 
	} 
	
	public int getFrameCount(){ 
	    return BMP_COLUMNS*BMP_ROWS; 
	} 
	
	public void setCurrentFrame(int frame){ 
	    currentFrame = frame; 
	    firstFrame = frame; 
	    lastFrame = frame+1; 
	} 
	
	public boolean setAnimation(int frame, int iframe, int lframe, int type){ 
	    if(frame < iframe || frame >= lframe || iframe >= lframe || type < 0 || type > 2) 
	            return false; 
	    currentFrame = frame; 
	    firstFrame = iframe; 
	    lastFrame = lframe; 
	    if(getFrameCount() > 1) 
	            animation = type; 
	    return true; 
	} 
	
	public boolean setAnimation(int type){ 
	    if(getFrameCount() > 1){ 
	            animation = type; 
	            return true; 
	    } 
	            else 
	                    return false;
	}
}