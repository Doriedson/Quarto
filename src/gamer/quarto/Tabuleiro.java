package gamer.quarto;

public class Tabuleiro {

	private static int turn_piece=0;
	private static int [][] tabuleiro = {{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
	private static byte[] pieces={170-256,169-256,166-256,165-256,154-256,153-256,150-256,149-256,106,105,146-256,145-256,90,89,86,85};
	private static int[] pieces_list={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
	/*redonda quadrado solida furada grande pequeno branca preta
	10101010=170 redonda solida grande branca
	10101001=169 redonda solida grande preta
	10100110=166 redonda solida pequena branca
	10100101=165 redonda solida pequena preta
	10011010=154 redonda furada grande branca
	10011001=153 redonda furada grande preta
	10010110=150 redonda furada pequena branca
	10010101=149 redonda furada pequena preta
	01101010=106 quadrado solida grande branca
	01101001=105 quadrado solida grande preta
	01100110=146 quadrado solida pequena branca
	01010101=145 quadrado solida pequena preta
	01011010=90  quadrado furada grande branca
	01011001=89  quadrado furada grande preta
	01010110=86  quadrado furada pequena branca
	01010101=85  quadrado furada pequena preta
	*/

	public Tabuleiro(){
		
	}

	public boolean play(int lin, int col){
		
		if(tabuleiro[lin][col]>0 || turn_piece==0){
			return false;
		} else {
			tabuleiro[lin][col]=turn_piece;
			turn_piece=0;
			return true;
		}
	}
	
	public boolean choosePiece(int pos){
		
		if( pieces_list[pos]>-1 ) {
			turn_piece=pos;
			pieces_list[pos]=-1;
			return true;
		} else {
			return false;
		}
		
	}	


}
