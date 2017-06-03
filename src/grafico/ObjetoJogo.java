package grafico;

import lombok.Getter;
import lombok.Setter;
import view.Campo;

@Getter
@Setter
public abstract class ObjetoJogo {

	private float x, y, w, h, direcao;
	private float aceleracao, velocidade;
	public void atualiza(Campo campo){
		reposiciona(campo);
		desenha(campo);
	}
	
	public abstract void desenha(Campo campo);
	public abstract void reposiciona(Campo campo);;
}
