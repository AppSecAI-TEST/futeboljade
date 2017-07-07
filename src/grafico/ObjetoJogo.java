package grafico;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import view.Campo;

import java.awt.*;
import java.awt.geom.Ellipse2D;

@Getter
@Setter
@Accessors(chain=true)
public abstract class ObjetoJogo {

	private static final int FOLGA_TESTE_COLISAO = 3;
	private double x, y, w, h, direcao;
	private double aceleracao, velocidade;
	private Campo campo;
	private Shape geometria;
	private int folgaTesteColisao = 0;
	private String info = "", infoDebug = "", infoPosicaoAtual = "";
	public static boolean DEBUG = true;

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
	
	Point getProximaPosicao(int distancia) {
		int x = (int) (getX() + Math.cos(Math.toRadians(getDirecao())) * distancia);
		int y = (int) (getY() + Math.sin(Math.toRadians(getDirecao())) * distancia);
		return new Point(x, y);
	}

	void desenha() {
		Graphics2D g2 = getCampo().g;
		desenha(g2);
	}

	protected void desenha(Graphics2D g2) {
		
	}

	protected void detectaColisao() {
		if (colidiuComLaterais() && getFolgaTesteColisao() == 0) {
			aoColidirComLaterais();
			setFolgaTesteColisao(FOLGA_TESTE_COLISAO);
		}
		if (getFolgaTesteColisao() > 0)
			setFolgaTesteColisao(getFolgaTesteColisao() - 1);
	}

	protected boolean colidiuComLaterais() {
		return getCampo()
				.getInfoAreasCampo()
				.getCampoNaoJogavel()
				.intersects(getGeometria().getBounds2D());
	}

	protected void aoColidirComLaterais() {
		inverterTragetoria();
		setAceleracao(getAceleracao() / 2);
	}

	void inverterTragetoria() {
		boolean colisaoLinhaFundo = getCampo()
				.getInfoAreasCampo()
				.getLinhaDeFundo()
				.intersects(getGeometria().getBounds2D());
		setDirecao((getDirecao() + (colisaoLinhaFundo ? 180 : 0)) * -1);
	}

	public void diminuiAceleracao() {
	}

	Shape getGeometria() {
		geometria = new Ellipse2D.Double(getX(), getY(), getW(), getH());
		return this.geometria;
	}

	public void apontarPara(double xdest, double ydest) {
		setDirecao(GeometriaUtil.getDirecaoPara(getX(), getY(), xdest, ydest));
	}
	
	public void setDirecao(double direcao){
		direcao = direcao % 360;
		if (direcao < 0){
		    direcao += 360;
		}
		this.direcao = direcao;
	}
}
