package grafico;

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

	public void atualiza() {
		reposiciona();
		desenha();
	}

	public abstract void desenha();

	public void reposiciona() {
		BigDecimal x = new BigDecimal(
				getX() + Math.cos(Math.toRadians(getDirecao())) * getVelocidade() * getAceleracao());
		BigDecimal y = new BigDecimal(
				getY() + Math.sin(Math.toRadians(getDirecao())) * getVelocidade() * getAceleracao());
		setX(x.floatValue());
		setY(y.floatValue());
	}

	public abstract void diminuiAceleracao();;
}
