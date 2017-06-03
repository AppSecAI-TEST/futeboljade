package grafico;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import view.Campo;

public class Bola extends ObjetoJogo {

	private static final int TAMANHO_BOLA = 30;
	private static final Color COR_BOLA = new Color(255, 255, 255);

	public Bola() {
		setW(TAMANHO_BOLA);
		setH(TAMANHO_BOLA);
	}

	@Override
	public void desenha(Campo campo) {
		Graphics2D g2 = campo.g;
		Ellipse2D.Double c = new Ellipse2D.Double(getX(), getY(), getW(), getH());
		g2.setColor(COR_BOLA);
		g2.fill(c);
	}

	@Override
	public void reposiciona(Campo campo) {
		setX(getX() + getVelocidade());
		setY(getY() + getVelocidade());
	}

}
