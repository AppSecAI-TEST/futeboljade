package grafico;

import java.awt.Rectangle;

import view.Campo;

public class PosicionadorJogador {

	private Campo campo;
	private int x = 0;
	private int y = 0;

	public PosicionadorJogador(Campo campo) {
		this.campo = campo;
	}
	
	public void posicionaJogadorVisitante(Jogador jogador) {
		Rectangle limitesCampoDireita = campo.getInfoAreasCampo().getLimitesCampoDireita();
		posicionar(jogador, limitesCampoDireita);
	}

	public void posicionaJogadorCasa(Jogador jogador) {
		Rectangle limitesCampoEsquerda = campo.getInfoAreasCampo().getLimitesCampoEsquerda();
		posicionar(jogador, limitesCampoEsquerda);
	}

	private void posicionar(Jogador jogador, Rectangle limitesCampoDireita) {
		Rectangle area = GeometriaUtil.getSubArea(limitesCampoDireita, 2, 3, x % 2, y % 3);
		jogador.setX(area.getCenterX() - jogador.getW() / 2).setY(area.getCenterY() - jogador.getH() / 2);
		x++;
		y++;
	}
}
