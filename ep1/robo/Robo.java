package robo;

/*********************************************************************/
/** ACH 2002 - Introducao à Análise de Algoritmos                   **/
/** EACH-USP - Segundo Semestre de 2018                             **/
/**                                                                 **/
/** <Felipe Marques> <nUsp>                                         **/
/**                                                                 **/
/*********************************************************************/

/**
	Classe que implementa os movimentos do robô.
*/
public class Robo implements IRobo {
	/** Coordenada x de início da busca */
	private static int x = ISala.X_INICIO_ARM;
	
	/** Coordenada y de início da busca */
	private static int y = ISala.Y_FIM_ARM+1;
	
	/** Mensageiro do robô **/
	public Mensageiro mensageiro;
	
	/** Tabuleiro do robô **/
	public Sala tabuleiro;
	
	/** Construtor padrão para o robô **/
	public Robo() {//Cria a sala e o mensageiro
		this.tabuleiro = new Sala();
		this.mensageiro = new Mensageiro();
	}
	
	boolean AchouBloco = false; //false: não achou bloco, true: achou bloco
	
	public boolean buscaBloco(int x, int y) {
		/* Próximas possições de X e Y. [0]: cima, [1]: direita, [2]: baixo, [3]: esquerda */
		int[] proxX = {0, 1, 0, -1};
		int[] proxY = {1, 0, -1, 0};
		
		mensageiro.mensagem(Mensageiro.BUSCA, x, y);
		
		if (tabuleiro.marcadorEm(x, y) == ISala.BLOCO_PRESENTE) { //Verifica se a posição atual possui bloco e se possuir manda mensagem de captura, muda a variável AchouBloco para true e retorna true para começar o retorno
			mensageiro.mensagem(Mensageiro.CAPTURA, x, y);
			tabuleiro.marcaPosicaoBusca(x, y, ISala.MARCA_PRESENTE);
			AchouBloco = true;
			return true;
		}
		tabuleiro.marcaPosicaoBusca(x, y, ISala.MARCA_PRESENTE);
		
		for(int sentido = 0; sentido < 4; sentido++) {
			if (tabuleiro.posicaoBuscaValida((x+proxX[sentido]), (y+proxY[sentido])) && tabuleiro.areaArmazenagem((x+proxX[sentido]), (y+proxY[sentido])) == false && tabuleiro.marcadorEm((x+proxX[sentido]), (y+proxY[sentido])) != ISala.MARCA_PRESENTE) { //Tenta ir pro sentido atual, verificando se a posição está dentro da sala, se não é área de armazenagem e se já passou por ali
				if (tabuleiro.marcadorEm((x+proxX[sentido]), (y+proxY[sentido])) == ISala.OBSTACULO_PRESENTE) { //Verifica se no sentido atual possui obstáculo e, se possuir, manda mensagem de obstáculo pro mensageiro
					mensageiro.mensagem(Mensageiro.OBSTACULO, (x+proxX[sentido]), (y+proxY[sentido]));
				}
				else buscaBloco((x+proxX[sentido]), (y+proxY[sentido])); //Se passar por todas as verificações significa que é possível ir para esta posição, então chama o método novamente para continuar a busca
				if (AchouBloco == true) {
					mensageiro.mensagem(Mensageiro.RETORNO, x, y);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean guardaBloco() { //Guarda os blocos encontrados nas posições 0,0; 1,0; 0,1; 1,1; respectivamente. Retorna true se conseguir guardar e false se estiver cheia
		for(int fixaY = 0; fixaY < 2; fixaY++) {
			for(int fixaX = 0; fixaX < 2; fixaX++) {
				if(tabuleiro.marcaPosicaoArmazenagem(fixaX, fixaY)) {
					mensageiro.mensagem(Mensageiro.ARMAZENAGEM, fixaX, fixaY);
					return true;
				}
			}
		}
		return false;
	}
	
	public void adicionaObstaculo(int x, int y) {
		if (tabuleiro.posicaoBuscaValida(x,y) && !tabuleiro.areaArmazenagem(x,y)) tabuleiro.sala[x][y] = ISala.OBSTACULO_PRESENTE; //Verifica se a posição está dentro da área de busca (dentro da sala e fora da área de armazenagem) e adiciona o obstáculo
	}
	
	public void adicionaBloco(int x, int y) {
		if (tabuleiro.posicaoBuscaValida(x, y) && !tabuleiro.areaArmazenagem(x, y)) tabuleiro.sala[x][y] = ISala.BLOCO_PRESENTE; //Verifica se a posição está dentro da área de busca (dentro da sala e fora da área de armazenagem) e adiciona o bloco
	}
	
	public void novaBusca() { //Remove todas as posições MARCA_PRESENTE do tabuleiro
		tabuleiro.removeMarcador(ISala.MARCA_PRESENTE);
	}
	
	public void buscaBlocos() { //Busca todos os 4 blocos pelo tabuleiro, se achar os 4 manda mensagem de fim, se não, manda mensagem de não achou
		int n = 0;
		for(; n < IRobo.N_BLOCOS; n++) {
			if (buscaBloco(x, y)) guardaBloco();
			else {
				mensageiro.msgNaoAchou();
				break;
			}
			novaBusca();
			AchouBloco = false;
		}
		if (n == 4) mensageiro.msgFim();
	}
	
	
	/**
		Retorna instância do mensageiro do robô
	*/
	public Mensageiro mensageiro() {
		return(this.mensageiro);
	}
}
