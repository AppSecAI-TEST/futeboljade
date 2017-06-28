package grafico;

import java.awt.Rectangle;

import lombok.Data;

@Data
public class Goleiro extends Jogador {
	
	private GrandeArea grandeAreaDefender;

	public Goleiro(Time time) {
		setColor(time.getCor());
		setTime(time);
	}
	
	@Override
	public void atualiza() {
		super.atualiza();
		verificaSeBolaEstaNaArea();
	}

	private void verificaSeBolaEstaNaArea() {
		Rectangle bola = getCampo().getBola().getGeometria().getBounds();
		Rectangle minhaArea = grandeAreaDefender.getLimites();
		if(minhaArea.contains(bola)){
			getCampo().getListeners().forEach(l->l.bolaEstaNaGrandeAreaDoTime(getTime().getNome()));
		}
	}
	
}
