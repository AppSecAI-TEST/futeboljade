package grafico;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import view.Campo;

@Getter
@Setter
@Accessors(chain=true)
public abstract class ObjetoJogo {

	private double x, y, w, h, direcao;
	private double aceleracao, velocidade;
	private Campo campo;
	private Shape bolaGrafica;
	private int folgaTesteColisao = 0;

	public void atualiza() {
		reposiciona();
		diminuiAceleracao();
		desenha();
		detectaColisao();
	}

	public void reposiciona() {
		setX(getX() + Math.cos(Math.toRadians(getDirecao())) * getVelocidade() * getAceleracao());
		setY(getY() + Math.sin(Math.toRadians(getDirecao())) * getVelocidade() * getAceleracao());
	}
	
	public Point getProximaPosicao(int distancia) {
		int x = (int) (getX() + Math.cos(Math.toRadians(getDirecao())) * distancia);
		int y = (int) (getY() + Math.sin(Math.toRadians(getDirecao())) * distancia);
		return new Point(x, y);
	}

	public void desenha() {
		Graphics2D g2 = getCampo().g;
		desenha(g2);
	}

	protected void desenha(Graphics2D g2) {
		
	}

	protected void detectaColisao() {
		if (colidiuComLaterais() && getFolgaTesteColisao() == 0) {
			aoColidirComLaterais();
			int tamanhoObjeto = (int) 3;
			setFolgaTesteColisao(tamanhoObjeto);
		}
		if (getFolgaTesteColisao() > 0)
			setFolgaTesteColisao(getFolgaTesteColisao() - 1);
	}

	protected boolean colidiuComLaterais() {
		return getCampo()
				.getInfoAreasCampo()
				.getCampoNaoJogavel()
				.intersects(getBolaGrafica().getBounds2D());
	}

	protected void aoColidirComLaterais() {
		inverterTragetoria();
		setAceleracao(getAceleracao() / 2);
	}

	protected void inverterTragetoria() {
		boolean colisaoLinhaFundo = getCampo()
				.getInfoAreasCampo()
				.getLinhaDeFundo()
				.intersects(getBolaGrafica().getBounds2D());
		setDirecao((getDirecao() + (colisaoLinhaFundo ? 180 : 0)) * -1);
	}

	public void diminuiAceleracao() {
	}

	public Shape getBolaGrafica() {
		bolaGrafica = new Ellipse2D.Double(getX(), getY(), getW(), getH());
		return this.bolaGrafica;
	}
	
	public void apontarPara(double xdest, double ydest){
		setDirecao(GeometriaUtil.getDirecaoPara(getX(), getY(), xdest, ydest));
	}
}
