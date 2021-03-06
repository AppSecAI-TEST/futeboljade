package grafico;

import view.Campo;

import java.awt.*;

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
		
		Rectangle limitesCampoEsquerda = campo.getInfoAreasCampo().getLimitesCampoEsquerda();
		guardaPosicaoAtaque(jogador, limitesCampoEsquerda, xVisitante, yVisitante);
		
		xVisitante ++;
		yVisitante ++;
	}

	public void posicionaJogadorCasa(Jogador jogador) {
		Rectangle limitesCampoEsquerda = campo.getInfoAreasCampo().getLimitesCampoEsquerda();
		posicionar(jogador, limitesCampoEsquerda, xCasa, yCasa);
		
		Rectangle limitesCampoDireita = campo.getInfoAreasCampo().getLimitesCampoDireita();
		guardaPosicaoAtaque(jogador, limitesCampoDireita, xCasa, yCasa);
		
		xCasa ++;
		yCasa ++;
	}

	private void posicionar(Jogador jogador, Rectangle limitesCampo, Integer x, Integer y) {
		Rectangle area = GeometriaUtil.getSubArea(limitesCampo, 2, 3, x % 2, y % 3);
		posicionaInicioJogo(jogador, area.getCenterX() - jogador.getW() / 2, area.getCenterY() - jogador.getH() / 2);
	}

	private void guardaPosicaoDefesa(Jogador jogador) {
		jogador.setPosicaoDefesa(new Point((int)jogador.getX(), (int)jogador.getY()));
	}
	
	private void guardaPosicaoAtaque(Jogador jogador, Rectangle limitesCampo, Integer x, Integer y) {
		Rectangle area = GeometriaUtil.getSubArea(limitesCampo, 2, 3, x % 2, y % 3);
		double xAtaque = area.getCenterX() - jogador.getW() / 2;
		double yAtaque = area.getCenterY() - jogador.getH() / 2;
		jogador.setPosicaoAtaque(new Point((int)xAtaque, (int)yAtaque));
	}

	public void posicionaGoleiroCasa(Jogador goleiro) {
		Rectangle limitesGrandeAreaEsquerda = campo.getInfoAreasCampo().getLimitesGrandeAreaEsquerda();
		double x = limitesGrandeAreaEsquerda.getMinX();
		double y = limitesGrandeAreaEsquerda.getMinY() + limitesGrandeAreaEsquerda.getHeight()/2 - goleiro.getW()/2;
		posicionaInicioJogo(goleiro, x, y);
		int xAtaque = (int) limitesGrandeAreaEsquerda.getMaxX();
		guardaPosicaoAtaque(goleiro, limitesGrandeAreaEsquerda, xAtaque, (int) y);
	}

	public void posicionaGoleiroVisitante(Jogador goleiro) {
		Rectangle limitesGrandeAreaDireita = campo.getInfoAreasCampo().getLimitesGrandeAreaDireita();
		double x = limitesGrandeAreaDireita.getMaxX()-goleiro.getW();
		double y = limitesGrandeAreaDireita.getMinY() + limitesGrandeAreaDireita.getHeight()/2 - goleiro.getW()/2;
		posicionaInicioJogo(goleiro, x, y);
		int xAtaque = (int) limitesGrandeAreaDireita.getMinX();
		guardaPosicaoAtaque(goleiro, limitesGrandeAreaDireita, xAtaque, (int) y);
	}

	private void posicionaInicioJogo(Jogador jogador, double x, double y) {
		jogador.setX(x).setY(y);
		guardaPosicaoDefesa(jogador);
	}
}
