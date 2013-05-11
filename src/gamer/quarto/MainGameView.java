package gamer.quarto;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.StrictMode;
import android.view.MotionEvent;

public class MainGameView extends GameView {
	
	protected int level = 1, score = 0;
	protected ArrayList<Pink> pinks; //Lista dos Pinks.
	protected ArrayList<Gold> golds; //Lista dos Golds.
	protected Background background;
	protected Sprite nextLevelSprite;
	
	private byte[] buffer;//recebe a comunicacao do servidor
	
	private long startTime;
	private Context context;
	private static boolean isServer;
	private static int jogador;

	private GameServer servidor;
	
	private Paint paintText;
	float scoreX,scoreY;
	
	//Botoes para mudar a tela para o tabuleiro ou para as peças
	private Sprite button[] = new Sprite[2];
	
	//Usando a câmera para saber qual cenário será exibido, tela menu, tabuleiro ou peças
	public static int camera=1;
	
	private int alivePinks;
	
	public MainGameView(Context context, boolean server) {
		super(context,2);
		this.context = context;
		System.out.println("Iniciando MainGame");
		
		isServer=server;
		
		System.out.println("Este é o servidor: " + server);
//libera a thread principal para esperar por uma resposta da rede
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy);
		
		if(isServer){
			System.out.println("Lado Servidor");
			servidor = new GameServer(this); //abre uma instância da thread servidor para esperar o cliente.
			servidor.start();
		} else {
			System.out.println("Lado Cliente");
			initClient();
		}

	}

	private void loadSplash(){

		((Activity)getContext()).finish();;
		
		Context ctx=getContext();

		((Activity)ctx).finish();

		Intent intent=new Intent(ctx, TitleActivity.class);

		ctx.startActivity(intent);
		
	}
	
	
	@Override
	protected void onLoad() {
		System.out.println("Carregando Jogo");

		Resources res=getResources();

		//tela 0 - Menu
		
		
		//tela 1 - Tabuleiro
		mSprites.add(new Background(BitmapFactory.decodeResource(res, R.drawable.space),getWidth(),getHeight(),1));
		mSprites.add(new Background(BitmapFactory.decodeResource(res, R.drawable.stars),getWidth(),getHeight(),1));
		
		button[0]=new Sprite(BitmapFactory.decodeResource(res, R.drawable.piece),1,1,1);
		
		button[0].x=getWidth()-60;
		button[0].y=getHeight()-40;
		button[0].width=60;
		button[0].height=40;

		Sprite tab;
		mSprites.add(tab=new Sprite(BitmapFactory.decodeResource(res, R.drawable.tabuleiro),1,1,1));
		tab.y=30;
		
		//mSprites.add(button[0]);

		//tela 2
		mSprites.add(new Sprite(BitmapFactory.decodeResource(res, R.drawable.pieces),1,1,2));
		
		button[1]=new Sprite(BitmapFactory.decodeResource(res, R.drawable.piece),1,1,2);

		button[1].x=0;
		button[1].y=getHeight()-40;
		button[1].width=60;
		button[1].height=40;
		
		//mSprites.add(button[1]);
		
		//Resources res = getResources();

		//Sprite title;
		
		//mSprites.add(title=new Sprite(BitmapFactory.decodeResource(res, R.drawable.splash),1,1));
		//title.x=0;
		//title.y=0;
	
	}

	private void erroClient(){
		AlertDialog alertDialog;
    	alertDialog = new AlertDialog.Builder(context).create();
    	alertDialog.setTitle("Nenhum Servidor encontrado!");
    	alertDialog.setMessage("Deseja tentar novamente?");
    	alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Sim", new DialogInterface.OnClickListener(){
    		public void onClick(DialogInterface dialog, int id) {
    			dialog.dismiss();
    			initClient();
			}
    	});
    	alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Não", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id) {
    			dialog.dismiss();
				loadSplash();
			}
    	});
    	
    	alertDialog.show();
	}

    private static String getIpAsString(InetAddress address) {
		byte[] ipAddress = address.getAddress();
		StringBuffer str = new StringBuffer();
		for(int i=0; i<3; i++) {
			if(i > 0) str.append('.');
			str.append(ipAddress[i] & 0xFF);				
		}
		return str.toString();
	}
	
	private static String receiveAnnounce() throws IOException {

    	byte[] local=null;
    	String local2="";
        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {

        	NetworkInterface intf = en.nextElement();
            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                InetAddress inetAddress = enumIpAddr.nextElement();
                if (!inetAddress.isLoopbackAddress()) {
                	//local=new String(inetAddress.getAddress(),0,4).getBytes("US-ASCII");
                	//System.out.println( getIpAsString(inetAddress) );
                	local = inetAddress.getHostAddress().getBytes("US-ASCII");
                	local2 = (getIpAsString(inetAddress) + ".1");
                }
            }
        }
		
		
        InetAddress group = InetAddress.getByName(local2);  

        System.out.println("Procurando Servidor");
          
        MulticastSocket socket = new MulticastSocket(40000);  
        socket.joinGroup(group);  

        try {  
            DatagramPacket packet;  
            byte[] buf = new byte[256];  
            packet = new DatagramPacket(buf, buf.length);  

			socket.setSoTimeout(1000);
            socket.receive(packet);  

            String received = new String(packet.getData(), packet.getOffset(), packet.getLength(), "US-ASCII");
            System.out.println(received);
            return received;  

        } finally {  
        	System.out.println("Nenhum servidor encontrado!");
            socket.leaveGroup(group);  
            socket.close();  
        }  
    }  

	private void initClient(){

		String IP="";
		
		try {  
            IP = receiveAnnounce(); //recebe o IP do servidor se houver um
            //System.out.println("IP do Servidor: " + IP);
        } catch (IOException ex) {
            //ex.printStackTrace();
        	erroClient(); //Se nao encontrou servidor exibe uma mensagem se quer tentar novamente ou retornar a tela de entrada
        	return;  //finaliza o metodo para poder exibir a mensagem na tela.
        }

		ClientGame clientGame=new ClientGame(IP,buffer,this); //inicia uma instancia da thread cliente para conectar ao servidor
		clientGame.start();
		
	}

	public void mensagem( byte[] msg){

		switch (msg[0]) {
			case '0':
				System.out.println("MGV: Conexão estabelecida.");
	
				//jogador=Integer.valueOf(new String(msg,1,1));
				System.out.println("id do jogador " + jogador);
				//Tabuleiro tabuleiro=new Tabuleiro();
				break;
			
			case'1':
				System.out.println("MGV: Jogo Começou");
				startGame();
				break;
		}
		
	}
	
	private void startGame(){

		//Resources res = getResources();

		//mSprites.add(new Sprite(BitmapFactory.decodeResource(res, R.drawable.space),1,1));


//		
//		title.x=getWidth()/2 - title.width/2;
		System.out.println("Background inicializado");
		
	}
	
	private void inicializa(){
		
		pinks = new ArrayList<Pink>();
		golds = new ArrayList<Gold>();
		
		Random random = new Random();
		Resources res = getResources();
		Bitmap bitmapPink = BitmapFactory.decodeResource(res, R.drawable.pink);
		Bitmap bitmapGold = BitmapFactory.decodeResource(res, R.drawable.gold_head);
		int limitPinkX = getWidth()-bitmapPink.getWidth()/6, // Define os limites da tela
				limitPinkY = getHeight()-bitmapPink.getHeight()/5, // que os pinks e golds podem
				limitGoldX = getWidth()-bitmapGold.getWidth()/8, // aparecer inicialmente.
				limitGoldY = getHeight()-bitmapGold.getHeight();
		//for(int i = 0; i < 10; i++)
			//pinks.add(new Pink(random.nextInt(limitPinkX), random.nextInt(limitPinkY), random, bitmapPink, 5, 6));
		//for(int i = 0; i < 10; i++)
			//golds.add(new Gold(random.nextInt(limitGoldX), random.nextInt(limitGoldY), random, bitmapGold, 1, 8));

		//mSprites.addAll(pinks); // Adiciona Pinks e Golds a lista de Sprites para serem 
		//mSprites.addAll(golds); // desenhados e atualizados automaticamente.
		
		paintText = new Paint();
		paintText.setColor(Color.WHITE);
		paintText.setTextSize(25);
		scoreX = getWidth()*0.05f;
		scoreY = getHeight()*0.95f;

		mSprites.add(nextLevelSprite = new Sprite(BitmapFactory.decodeResource(res, R.drawable.nextlevel),1,1,1));
		nextLevelSprite.x = getWidth()/2 - nextLevelSprite.width/2;
		nextLevelSprite.y = (int) (getHeight()*0.2f);
		nextLevelSprite.visible = false;

		newStage();
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);		
		
		//if(nextLevelSprite.visible)
			//canvas.drawText(context.getString(R.string.nextlevel_msg), 50, getHeight()*0.7f, paintText);
	}

	private void newStage() {
		//204x320
		//System.out.println(getWidth() + " x " + getHeight() );
		
		level++; //Passa de level
		nextLevelSprite.visible = false; //Deixa a mensagem e imagem de proximo level invisiveis
		alivePinks = 5+level; //Define a quantidade de Pinks de acordo com o level
		startTime = System.currentTimeMillis(); // Guarda o tempo do inicio do level

		Random random = new Random();
		Resources res = getResources();
		Bitmap bitmapPink = BitmapFactory.decodeResource(res, R.drawable.pink);
		Bitmap bitmapGold = BitmapFactory.decodeResource(res, R.drawable.gold_head);
		Pink pink;
		Gold gold;
		int limitPinkX = getWidth()-bitmapPink.getWidth()/6,
				limitPinkY = getHeight()-bitmapPink.getHeight()/5,
				limitGoldX = getWidth()-bitmapGold.getWidth()/8,
				limitGoldY = getHeight()-bitmapGold.getHeight();
		//for(int i = 0; i < alivePinks; i++){ // Cria Pinks
			//pinks.add(pink = new Pink(random.nextInt(limitPinkX), random.nextInt(limitPinkY), random, bitmapPink, 5, 6));
			//pink.speed = level; // Velocidade de acordo com o level
		//}
		//golds.add(gold = new Gold(random.nextInt(limitGoldX), random.nextInt(limitGoldY), random, bitmapGold, 1, 8)); // Cria mais um Gold
		//gold.speedX *= 1+level/3; // Gold criado pode ser mais rapido que o normal
		//gold.speedY *= 1+level/3;
		//mSprites.addAll(1, pinks); //Adiciona os Sprites criado se preocupado com a ordem da lista pois influencia na ordem de desenho
		//mSprites.add(mSprites.size()-1, gold);
	}	
	
	@Override
	public void update() {
		super.update();
/*	Pink pink;
	for(int i = 0; i < pinks.size(); i++){
		pink = pinks.get(i);
		if(pink.dead){ // Se ta morto, retira das listas
			mSprites.remove(pink);
			pinks.remove(pink);
			i--;
		}else if(!pink.isDead()){
			if(pink.x < 0 && pink.direction >= 5 && pink.direction <= 7)
				pink.changeDirection((byte) (4 - pink.direction % 4));
			else if(pink.x > getWidth()-pink.width && pink.direction >= 1 && pink.direction <= 3)
				pink.changeDirection((byte) (8 - pink.direction));
			else if(pink.y < 0 && pink.direction >= 3 && pink.direction <= 5)
				pink.changeDirection((byte) ((12 - pink.direction) % 8));
			else if(pink.y > getHeight()-pink.height && (pink.direction >= 7 || pink.direction <= 1))
				pink.changeDirection((byte) (5 - (pink.direction+1) % 8));
		}
	}	
	
	for(Gold gold : golds) // Para cada Gold, verifica se ele chegou nas bordas da tela e 
		if(gold.x < 0 || gold.x > getWidth()-gold.width) // muda de direção caso ocorra. 
			gold.speedX *= -1;
		else if(gold.y < 0 || gold.y > getHeight()-gold.height)
			gold.speedY *= -1;
			*/
	}

	@Override
	public void TouchEvents(MotionEvent event) {
		if((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN){
			if(camera==1)
				camera=2;
			else
				camera=1;
/*			if(alivePinks > 0){ // Se tiver pinks vivos, verifica se clicou em pink ou gold
				float x = event.getX(), y = event.getY();
				for(Gold gold : golds) // Para cada gold...
					if(x > gold.x && y > gold.y && x < gold.x + gold.width && y < gold.y + gold.height){ // Verifica de clicou nele...
						((Activity) context).finish(); // Se clicou termina a activity...
						Intent intent = new Intent(context, TitleActivity.class); // Chama o GameOverActivity...
						//intent.putExtra("SCORE", score); // Passa o score como parâmetro
						context.startActivity(intent);
					}
				for(Pink pink : pinks) // Para cada pink...
					if(!pink.isDead() && x > pink.x && y > pink.y && x < pink.x + pink.width && y < pink.y + pink.height){ // Se o pink estiver vivo e tocar nele...
						pink.kill(); // Mata o pink...
						//int add;
						score += (int) Math.max(100 - level*3 - (System.currentTimeMillis() - startTime)/500, 1); // Adiciona o score dependendo de qual rapido matou o pink...
						alivePinks--; // Subtrai um do contador de pinks vivos...
						if(alivePinks == 0) // Se não tem mais nenhum vivo...
							nextLevelSprite.visible = true; // Exibe imagem de proximo nível
					//	Log.d("Score", "Valeu: " + add);
					}
			}else{ // Sem pink vivo, a mensagens de proximo level já está exibida e tocar significa passar de level
				newStage();
			}*/
		}
	}
}