package grafico;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.math.BigDecimal;

public class Bola extends ObjetoJogo {

	private static final double FATOR_ACELERACAO = 0.025;
	private static final int TAMANHO_BOLA = 15;
	private static final Color COR_BOLA = new Color(255, 255, 255);

	public Bola() {
		setW(TAMANHO_BOLA);
		setH(TAMANHO_BOLA);
	}

	@Override
	public void desenha(Graphics2D g2) {
		g2.setColor(COR_BOLA);
		g2.fill(getBolaGrafica());
	}

	@Override
	public void reposiciona() {
		Jogador jogadorComBola = getCampo().getJogadorComBola();
		if(jogadorComBola == null){
			super.reposiciona();
			diminuiAceleracao();
		}else{
			reposicionaEmRelacaoA(jogadorComBola);
		}
	}
	
	public void reposicionaEmRelacaoA(Jogador jogador) {
		Point proximaPosicao = jogador.getProximaPosicao(20);
		setX(proximaPosicao.getX()+10);
		setY(proximaPosicao.getY()+10);
		setVelocidade(jogador.getVelocidade());
	}

	@Override
	protected void detectaColisao() {
		super.detectaColisao();
		InfoAreasCampo areas = getCampo().getInfoAreasCampo();
		if (areas.getLimitesGolDireita().contains(getBolaGrafica().getBounds())) {
			getCampo().gooolTimeEsquerda();
			setVelocidade(1);
		}
		if (areas.getLimitesGolEsquerda().contains(getBolaGrafica().getBounds())) {
			getCampo().gooolTimeDireita();
			setVelocidade(1);
		}
	}

	public void diminuiAceleracao() {
		if (getAceleracao() > 0)
			setAceleracao((float) (getAceleracao() - FATOR_ACELERACAO));
	};
}
