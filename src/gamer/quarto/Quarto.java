package gamer.quarto;

public class Quarto extends Tabuleiro {

	private static int turn_player=0;

	public Quarto(){
		
		//System.out.println(pecas[0] & pecas[8] & 0xff);
		
	}
	
	public int getTurn(){
	
		return turn_player;
	}
	
}
