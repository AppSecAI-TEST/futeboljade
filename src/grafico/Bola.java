package grafico;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Rectangle2D;

public class Bola extends ObjetoJogo {

	private static final double FATOR_ACELERACAO = 0.05;
	private static final int TAMANHO_BOLA = 15;
	private static final Color COR_BOLA = new Color(255, 255, 255);
	private Double bolaGrafica;
	private int folgaTesteColisao = 0;

	public Bola() {
		setW(TAMANHO_BOLA);
		setH(TAMANHO_BOLA);
	}
	
	@Override
	public void atualiza() {
		super.atualiza();
		detectaColisao();
	}

	private void detectaColisao() {
		Rectangle2D ateOndeABolaPodeIr = 
		new Rectangle2D.Double(
				getCampo().getLimites().getX()+getW(),
				getCampo().getLimites().getY()+getH(), 
				getCampo().getLimites().getWidth()-2*getW(),
				getCampo().getLimites().getHeight()-2*getH());
		
		Rectangle golEsquerda = getCampo().getGolEsquerda().getLimites();
		Rectangle golDireita = getCampo().getGolDireita().getLimites();
		
		if(golDireita.contains(bolaGrafica.getBounds())){
			getCampo().gooolTimeEsquerda();
			return;
		}
		
		if(golEsquerda.contains(bolaGrafica.getBounds())){
			getCampo().gooolTimeDireita();
			return;
		}
		
		boolean batendoNaLinha = !ateOndeABolaPodeIr.intersects(bolaGrafica.getBounds2D());
		boolean gol = getCampo().getStatus() == StatusJogo.GOOOL;
		if(batendoNaLinha && !gol && folgaTesteColisao == 0){
			boolean colisaoLinhaFundo = getX() <= ateOndeABolaPodeIr.getX() || getX() >= (ateOndeABolaPodeIr.getX()+ateOndeABolaPodeIr.getWidth());
			setDirecao((getDirecao()+(colisaoLinhaFundo?180:0))*-1);
			setAceleracao(getAceleracao() / 2);
			folgaTesteColisao = 3;
		}
		if(folgaTesteColisao > 0)
			folgaTesteColisao--;
	}

	@Override
	public void desenha() {
		Graphics2D g2 = getCampo().g;
		Ellipse2D.Double bola = new Ellipse2D.Double(getX(), getY(), getW(), getH());
		this.bolaGrafica = bola;
		g2.setColor(COR_BOLA);
		g2.fill(bola);		
	}
	
	@Override
	public void reposiciona() {
		super.reposiciona();
		diminuiAceleracao();
	}
	
	public void diminuiAceleracao() {
		if (getAceleracao() > 0)
			setAceleracao((float) (getAceleracao() - FATOR_ACELERACAO));
	};
}
