package grafico;

import java.awt.Rectangle;

import lombok.Setter;

public class Goleiro extends Jogador {
	
	@Setter
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
		if(bolaEstaNaArea()){
			getCampo().getListeners().forEach(l->l.bolaEstaNaGrandeAreaDoTime(getTime().getNome()));
		}
	}

	private boolean bolaEstaNaArea() {
		Rectangle bola = getCampo().getBola().getGeometria().getBounds();
		Rectangle minhaArea = grandeAreaDefender.getLimites();
		return minhaArea.contains(bola);
	}

}
