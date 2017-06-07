package grafico;

import java.awt.Color;
import java.awt.Graphics2D;

public class Bola extends ObjetoJogo {

	private static final double FATOR_ACELERACAO = 0.05;
	private static final int TAMANHO_BOLA = 15;
	private static final Color COR_BOLA = new Color(255, 255, 255);

	public Bola() {
		setW(TAMANHO_BOLA);
		setH(TAMANHO_BOLA);
	}

	@Override
	public void desenha() {
		Graphics2D g2 = getCampo().g;
		g2.setColor(COR_BOLA);
		g2.fill(getBolaGrafica());
	}

	@Override
	public void reposiciona() {
		super.reposiciona();
		diminuiAceleracao();
	}

	@Override
	protected void detectaColisao() {
		super.detectaColisao();
		InfoAreasCampo areas = getCampo().getInfoAreasCampo();
		if (areas.getGolDireita().contains(getBolaGrafica().getBounds())) {
			getCampo().gooolTimeEsquerda();
		}
		if (areas.getGolEsquerda().contains(getBolaGrafica().getBounds())) {
			getCampo().gooolTimeDireita();
		}
	}

	public void diminuiAceleracao() {
		if (getAceleracao() > 0)
			setAceleracao((float) (getAceleracao() - FATOR_ACELERACAO));
	};
}
