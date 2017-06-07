package grafico;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import view.Campo;

@Getter
@Setter
public abstract class ObjetoJogo {

	private float x, y, w, h, direcao;
	private float aceleracao, velocidade;
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
		BigDecimal x = new BigDecimal(
				getX() + Math.cos(Math.toRadians(getDirecao())) * getVelocidade() * getAceleracao());
		BigDecimal y = new BigDecimal(
				getY() + Math.sin(Math.toRadians(getDirecao())) * getVelocidade() * getAceleracao());
		setX(x.floatValue());
		setY(y.floatValue());
	}

	public void desenha() {
	}

	protected void detectaColisao() {
		if (colidiuComLaterais() && getFolgaTesteColisao() == 0) {
			aoColidirComLaterais();
			setFolgaTesteColisao(3);
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
}
