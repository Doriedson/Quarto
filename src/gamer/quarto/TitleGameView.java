package gamer.quarto;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class TitleGameView extends GameView {

	private Paint paintText;
	private Context context;
	
	public TitleGameView(Context context){
		super(context,1);
		this.context=context;
		
		
	}
	
	@Override
	public void TouchEvents(MotionEvent event){
		if ( (event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN){

			float y = event.getY();
			
			if( y >= getHeight()*0.6f & y < getHeight()*0.8f ) {
				Context ctx=getContext();
				//gameLoopThread.setRunning(false);
				((Activity)ctx).finish();

				Intent intent=new Intent(ctx, MainGameActivity.class);
				intent.putExtra("servidor",true);

				ctx.startActivity(intent);

				
			} else if ( y >= getHeight()*0.8f ) {
				Context ctx=getContext();
				//gameLoopThread.setRunning(false);
				((Activity)ctx).finish();
				Intent intent=new Intent(ctx, MainGameActivity.class);
				
				intent.putExtra("servidor",false);
				ctx.startActivity(intent);
			}
		}
	}
	
	@Override
	protected void onLoad(){

	    String ip_address=null;
	    
	     System.setProperty("java.net.preferIPv4Stack", "true"); 
	        try
	        {
	          Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces();
	          while (niEnum.hasMoreElements())
	          {
	            NetworkInterface ni = niEnum.nextElement();
	            if(!ni.isLoopback()){
	                for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses())
	                {
	                  ip_address = interfaceAddress.getAddress().toString().substring(1);
	                }
	            }
	          }
	        }
	        catch (SocketException e)
	        {
	          e.printStackTrace();
	        }
	        
	        if( ip_address==null ) {
	        	AlertDialog alertDialog;
	        	alertDialog = new AlertDialog.Builder(context).create();
	        	alertDialog.setTitle("Nenhuma Rede Detectada!");
	        	alertDialog.setMessage("Para jogar é preciso estar conectado a uma rede.");
	        	alertDialog.setButton(-1,"OK", new DialogInterface.OnClickListener(){

						public void onClick(DialogInterface dialog, int id) {
							gameLoopThread.setRunning(false);
							((Activity)getContext()).finish();;							
						}
	        	});
	        	alertDialog.show();
	        }
		
		
		Resources res=getResources();
		Sprite title;
		mSprites.add(title=new Sprite(BitmapFactory.decodeResource(res, R.drawable.splash),1,1,1));
		title.x=getWidth()/2 - title.width/2;
		title.y=30;
		
		paintText=new Paint();
		paintText.setColor(Color.BLACK);
		paintText.setTextSize(20);
		
		gameLoopThread.setRunning(false);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.drawText(context.getString(R.string.title),getWidth()/2-20,20,paintText);
		canvas.drawText(context.getString(R.string.new_game),50,getHeight()*0.65f,paintText);
		canvas.drawText(context.getString(R.string.enter_game),50,getHeight()*0.85f,paintText);
	}
}
