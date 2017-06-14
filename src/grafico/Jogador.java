package grafico;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain=true)
public class Jogador extends ObjetoJogo {
	
	public static final Color COR_CASA = new Color(255, 51, 51);
	public static final Color COR_VISITANTE = new Color(0, 170, 255);
	public static final int TAMANHO_JOGADOR = 30;
	
	private Color color;
	private Time time;
	private String nome;
	
	public Jogador() {
		setW(TAMANHO_JOGADOR);
		setH(TAMANHO_JOGADOR);
	}
	
	public Jogador(String nome, Color color){
		this();
		this.nome = nome;
		this.color = color;
	}
	
	@Override
	protected void desenha(Graphics2D g2) {
		g2.setColor(getColor());
		g2.fill(getBolaGrafica());
	}
	
	@Override
	protected void detectaColisao() {
		super.detectaColisao();
		if(colidiuComBola()){
			aoColidirComBola();
		}
	}
		
	private void aoColidirComBola() {
		getCampo().jogadorColidiuComBola(this);
	}

	private boolean colidiuComBola() {
		Shape bola = getCampo().getBola().getBolaGrafica();
		return getBolaGrafica().intersects(bola.getBounds());
	}
	
	protected boolean colidiuComLaterais() {
		return getCampo()
				.getInfoAreasCampo()
				.getLaterais()
				.intersects(getBolaGrafica().getBounds2D());
	}

	@Override
	protected void aoColidirComLaterais() {
		inverterTragetoria();
	}

}
