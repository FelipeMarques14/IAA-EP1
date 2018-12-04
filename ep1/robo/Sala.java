package robo;

/**
	Classe que define a sala por onde o robô passeia. A sala é uma matriz quadrada de largura ISala.TAMANHO_SALA.
	
	Essa classe deve ser implementada pelo aluno
*/
public class Sala implements ISala {

	public int sala[][];

	public Sala () {
		this.sala = new int[ISala.TAMANHO_SALA][ISala.TAMANHO_SALA]; //Matriz quadrada de tamanho ISala.TAMANHO_SALA (10)
	}
	
	public boolean posicaoBuscaValida(int x, int y) {
		if (x < 10 && x >= 0 && y < 10 && y >= 0) return true; //Verifica se a posição está dentro do tabuleiro
		return false;
	}
	
	public int marcadorEm(int x, int y) { //Retorna o marcador presente na posição passada
		return sala[x][y];
	}
	
	public boolean marcaPosicaoBusca(int x, int y, int marcador) {
		if (posicaoBuscaValida(x,y) && !areaArmazenagem(x,y)) { //Verifica se a posição é válida e se não está dentro da área de armazenagem e, caso verdadeiro, marca a posição passada com o marcador passado
			sala[x][y] = marcador;
			return true;
		}
		return false;
	}
	
	public boolean marcaPosicaoArmazenagem(int x, int y) {
		if (areaArmazenagem(x,y) && sala[x][y] != ISala.BLOCO_PRESENTE) { //Verifica se a posição é na área de armazenagem e se já não possui um bloco e, caso verdadeiro, marca a posição com BLOCO_PRESENTE
			sala[x][y] = ISala.BLOCO_PRESENTE;
			return true;
		}
		return false;
	}
	
	public void removeMarcador(int x, int y) {
		if (posicaoBuscaValida(x,y) && sala[x][y] != ISala.OBSTACULO_PRESENTE) { //Verifica se a posição é válida e se não possui obstáculo e, caso verdadeiro, troca a posição passada por POSICAO_VAZIA
			sala[x][y] = ISala.POSICAO_VAZIA;
		}
	}
	
	public boolean areaArmazenagem(int x, int y) {
		if (x >= 0 && y >= 0) { //Verifica se está dentro da área de armazenagem (x = 0 ou 1, y = 0 ou 1)
			if (x < 2 && y < 2) return true;
		}
		return false;
	}
	
	public void removeMarcador(int marcador) { //Verifica em toda a sala se existe tal marcador e, caso existir, transforma em POSICAO_VAZIA
		for (int i = 0; i < sala.length; i++) {
			for (int j = 0; j < sala[0].length; j++) {
				if (sala[i][j] == marcador) removeMarcador(i, j);
			}
		}
	}
}
