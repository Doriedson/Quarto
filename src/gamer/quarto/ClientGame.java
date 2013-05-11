package gamer.quarto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

public class ClientGame extends Thread {

	private Socket socket;
	private byte[] buffer, bufferClient; //buffer para receber do servidor e buffer do cliente que recebera uma mensagem por vez;
	private MainGameView mGameView;
	
	public ClientGame(String IP, byte[] buffer, MainGameView mGameView){
		
		this.bufferClient=buffer;
		this.mGameView = mGameView;
		
		System.out.println("Tentando conectar ao servidor");

		try {
			socket=new Socket(IP,4000);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Conectado ao Servidor");

	}

	
    public void run() {  
        
		try {
			
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();

//			byte[] dadosServidor = new byte[100];
			byte[] dadosServidor = null;
			byte[] input=null;

			int i, indiceInicialBuffer, tam;

			do{
				//Arrays.fill(dadosServidor, (byte) 0); //zera o vetor antes de ler para evitar que tenha ficado algum lixo
				System.out.println("Aguardando dados do servidor");
				in.read(dadosServidor);

				for (i=0; i < dadosServidor.length && dadosServidor[i] != 0; ++i); //procura pelo primeiro indice zero
					if (buffer == null) {
						buffer=Arrays.copyOfRange(dadosServidor, 0, i); //copia de dadosServidor, mas soh ate o 1o zero (exclusivo), assim, soh ficamos com os dados de verdade
					}
					else {
						tam = buffer.length;
						buffer = Arrays.copyOf(buffer, tam + i); //buffer recebe ele mesmo e eh aumentado para receber (abaixo) os dados de dadosServidor
						System.arraycopy(dadosServidor, 0, buffer, tam+1, i); //copia de dadosServidor, mas soh ate o 1o zero (exclusivo), assim, soh ficamos com os dados de verdade
					}

					for (i=0, indiceInicialBuffer=0; i < buffer.length; ++i) {
						if (buffer[i] == '|') {
							input = Arrays.copyOfRange(buffer, indiceInicialBuffer , i);
							
						mGameView.mensagem(input);
						System.out.println("Mensagem Servidor: " + new String(input,0,input.length));

						//Resources res = mGameView.getResources();
//						Sprite title;
						
//						Options options=new BitmapFactory.Options();
//						options.inSampleSize=0;
//						Bitmap bmp=BitmapFactory.decodeResource(res, R.drawable.tabuleiro);
//						System.out.println("bmp " + bmp.getWidth() );
						//mGameView.mSprites.add(new Sprite(BitmapFactory.decodeResource(res, R.drawable.tabuleiro),1,1));
						
//						System.out.println(getWidth() + "x" + getHeight());
						//if (!status.equals("")) {
							//out.write(status.getBytes());
						//}

						indiceInicialBuffer = i+1;
					}
				}

				if (buffer[i-1] == '|') { //soh reinicio o buffer se a ultima coordenada do buffer tiver sido um pipe, indicando que a informacao veio inteira
					buffer = null;
				}

			} while (true);//!partida.fimDeJogo);

			//s.close();
		}
		catch (IOException e){
			System.err.println("Ocorreu um erro com a conexão com o Servidor! " + e);
			System.exit(0);
			//System.err.println(err);
		}

    }
	
}
