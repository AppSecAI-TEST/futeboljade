package grafico;

import java.awt.Rectangle;

import view.Campo;

public class PosicionadorJogador {

	private Campo campo;
	private Integer xCasa = 0;
	private Integer yCasa = 0;
	private Integer xVisitante = 0;
	private Integer yVisitante = 0;

	public PosicionadorJogador(Campo campo) {
		this.campo = campo;
	}
	
	public void posicionaJogadorVisitante(Jogador jogador) {
		Rectangle limitesCampoDireita = campo.getInfoAreasCampo().getLimitesCampoDireita();
		posicionar(jogador, limitesCampoDireita, xVisitante, yVisitante);
		xVisitante ++;
		yVisitante ++;
	}

	public void posicionaJogadorCasa(Jogador jogador) {
		Rectangle limitesCampoEsquerda = campo.getInfoAreasCampo().getLimitesCampoEsquerda();
		posicionar(jogador, limitesCampoEsquerda, xCasa, yCasa);
		xCasa ++;
		yCasa ++;
	}

	private void posicionar(Jogador jogador, Rectangle limitesCampoDireita, Integer x, Integer y) {
		Rectangle area = GeometriaUtil.getSubArea(limitesCampoDireita, 2, 3, x % 2, y % 3);
		jogador.setX(area.getCenterX() - jogador.getW() / 2).setY(area.getCenterY() - jogador.getH() / 2);
	}
}
