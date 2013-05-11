package gamer.quarto;

import java.io.*;
import java.util.*;
import java.net.*;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

public class ServerGameView extends GameView {

	private Paint paintText;
	private Context context;
	static String ip_address=null;
	private ServerSocket server;
	private static String TAG = "ServerSocketTest";
	
	public ServerGameView(Context context){
		super(context,3);
		this.context=context;
	}

	private static void startAnnounce() {  
        Thread thread = new Thread(new Runnable() {  
             
            public void run() {  
                try {  
                	byte[] local=null;
        	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {

        	        	NetworkInterface intf = en.nextElement();
        	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
        	                InetAddress inetAddress = enumIpAddr.nextElement();
        	                if (!inetAddress.isLoopbackAddress()) {
        	                	local = inetAddress.getHostAddress().getBytes("US-ASCII");
        	                }
        	            }
        	        }

                    DatagramSocket socket = new DatagramSocket(40000);  
                    try {  
                        InetAddress group = InetAddress.getByName("224.0.0.123");  
                        DatagramPacket packet = new DatagramPacket(local, local.length, group, 40000);

                        System.out.println("IP Servidor " + new String(packet.getData(), packet.getOffset(), packet.getLength(), "US-ASCII"));

                        while (true) {  
                            socket.send(packet);  
                            Thread.sleep(1000);  
                        }  
                    } finally {  
                        socket.close();  
                    }  
                } catch (InterruptedException ex) {  
                    Thread.currentThread().interrupt();  
                } catch (IOException ex) {  
                    ex.printStackTrace();  
                }  
            }  
        });  
        thread.setDaemon(true);  
        thread.start();  
    } 
	
	@Override
	public void TouchEvents(MotionEvent event){
		if ( (event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN){

			float y = event.getY();
			
			if( y >= getHeight()*0.6f & y < getHeight()*0.8f ) {
				
			} else if ( y >= getHeight()*0.8f ) {
//				Context ctx=getContext();
//				((Activity)ctx).finish();;
//				Intent intent=new Intent(ctx, MainGameActivity.class);
//				ctx.startActivity(intent);
			}
		}
	}
	
	@Override
	protected void onLoad(){

	    //String bcast_address=null;
/*
	    System.setProperty("java.net.preferIPv4Stack", "true"); 

	    try
	        {
	          Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces();
	  	    System.out.println("enumerate");
	          while (niEnum.hasMoreElements())
	          {
	            NetworkInterface ni = niEnum.nextElement();

	            if(!ni.isLoopback()){
	                for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses())
	                {

	                  //bcast_address = interfaceAddress.getBroadcast().toString();
	                  ip_address = interfaceAddress.getAddress().toString();
	                  System.out.println("enumerando");


	                }
	            }
	          }
	        }
	        catch (SocketException e)
	        {
	          e.printStackTrace();
	        }

	        System.out.println( ip_address);
			
//envia o ip do servidor por multicast */
	        startAnnounce();
	        System.out.println("startAnnounce");


		Runnable conn = new Runnable() {
	        public void run() {
	            try {
	                server = new ServerSocket(40000);

	                while (true) {
	                	Quarto teste=new Quarto();
	                	System.out.print("Servidor inicializado com sucesso!\nAguardando jogador\n" );	                	
	                    Socket socket = server.accept();
	                    System.out.println("Jogador conectado!");
	                    
	                    InputStream clienteIn = null;
	                    OutputStream clienteOut = null;

	                    clienteIn = socket.getInputStream();
	                    clienteOut = socket.getOutputStream();

	                    byte[] dadosCliente = new byte[100];

	                    //while (true) {
                               //envia status de conexao ok para o jogador 1 e pede pra aguardar.
                        clienteOut.write("0|".getBytes());
	                     //}
	                    socket.close();
	                }
	            } catch (IOException e) {
	                Log.e(TAG, e.getMessage());
	            } catch (Exception e) {             
	                Log.e(TAG, e.getMessage());
	            }
	        }
	    };		
		
	    new Thread(conn).start();		
	   
//       String[] status;

              
       //byte[][] buffer = new byte[2][];
       //buffer[0]=null;
       //buffer[1]=null;
       //byte[] input=null;

       //JogoServidor partida = new JogoServidor();
      // int i, indiceInicialBuffer, tam;

           //System.out.print("Servidor inicializado com sucesso!\nAguardando jogador 2\n");
           //con[1] = s.accept();
           //in[1] = con[1].getInputStream();
           //out[1] = con[1].getOutputStream();

           //envia status de conexao ok para o jogador 2
           //out[1].write("0|".getBytes());

           //pede o nome dos jogadores
           //out[0].write("2|".getBytes());
           //out[1].write("2|".getBytes());
           
//           do{
  //             for ( int jog=0; jog<2; jog++){
    //               Arrays.fill(dadosCliente, (byte) 0); //zera o vetor antes de ler para evitar que tenha ficado algum lixo
//                   if (in[jog].available() != 0) {
  //                     in[jog].read(dadosCliente);
    //               	System.out.print(new String(dadosCliente));

      //                 for (i=0; i < dadosCliente.length && dadosCliente[i] != 0; ++i); //procura pelo primeiro indice zero
                   
        //               if (buffer[jog] == null) {
          //                 buffer[jog]=Arrays.copyOfRange(dadosCliente, 0, i); //copia de dadosServidor, mas soh ate o 1o zero (exclusivo), assim, soh ficamos com os dados de verdade
            //           }
              //         else {
                //           tam = buffer[jog].length;
                  //         buffer[jog] = Arrays.copyOf(buffer[jog], tam + i); //buffer recebe ele mesmo e eh aumentado para receber (abaixo) os dados de dadosServidor
                    //       System.arraycopy(dadosCliente, 0, buffer[jog], tam+1, i); //copia de dadosServidor, mas soh ate o 1o zero (exclusivo), assim, soh ficamos com os dados de verdade
      //                 }
//
//                       for (i=0, indiceInicialBuffer=0; i < buffer[jog].length; ++i) {
 //                          if (buffer[jog][i] == '|') {
  //                             input = Arrays.copyOfRange(buffer[jog], indiceInicialBuffer , i);
                               //status = partida.executa(input,jog);

                              // if (!status[jog].equals("")) {
                                //   out[jog].write(status[jog].getBytes());
                              //}
                              // if (!status[1-jog].equals("")) {
                               //    out[1-jog].write(status[1-jog].getBytes());
                              //}

    //                           indiceInicialBuffer = i+1;
     //                      }
      //                 }

 //                      if (buffer[jog][i-1] == '|') { //soh reinicio o buffer se a ultima coordenada do buffer tiver sido um pipe, indicando que a informacao veio inteira
  //                         buffer[jog] = null;
   //                    }
   //                }
    //           }
//           } while (true);//!partida.fimDeJogo);
           
			//con[0].close();
			//con[1].close();
			//partida = new JogoServidor(); //reinicia a partida
//          }


		//Resources res=getResources();
//		Sprite title;
//		mSprites.add(title=new Sprite(BitmapFactory.decodeResource(res, R.drawable.title),1,1));
//		title.x=getWidth()/2 - title.width/2;
//		title.y=30;
		
		//paintText=new Paint();
		//paintText.setColor(Color.BLACK);
		//paintText.setTextSize(20);
		
		
		/*
		
		try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {

	        	NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress()) {
	                    System.out.println( inetAddress.getHostAddress().toString() );
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        System.out.println( ex.toString() );
	    }		
		
*/
		
		
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
//		canvas.drawText(context.getString(R.string.title_activity_truco),getWidth()/2-20,20,paintText);
//		canvas.drawText(context.getString(R.string.new_game),50,getHeight()*0.65f,paintText);
//		canvas.drawText(context.getString(R.string.enter_game),50,getHeight()*0.85f,paintText);
	}
	
}
