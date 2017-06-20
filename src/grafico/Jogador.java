package grafico;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import jogo.MovimentoBola;
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
		g2.fill(getGeometria());
	}
	
	@Override
	protected void detectaColisao() {
		super.detectaColisao();
		if(colidiuComBola()){
			aoColidirComBola();
		}
		if(colidiuComJogador()){
			aoColidirComJogador();
		}
	}
		
	private void aoColidirComJogador() {
		setVelocidade(0);
	}

	private boolean colidiuComJogador() {
		for(Jogador j : getCampo().getJogadores()){
			if(j != this && j.getGeometria().intersects(this.getGeometria().getBounds())){
				return true;
			}
		}
		return false;
	}

	private void aoColidirComBola() {
		getCampo().jogadorColidiuComBola(this);
	}

	private boolean colidiuComBola() {
		Shape bola = getCampo().getBola().getGeometria();
		return getGeometria().intersects(bola.getBounds());
	}
	
	protected boolean colidiuComLaterais() {
		return getCampo()
				.getInfoAreasCampo()
				.getLaterais()
				.intersects(getGeometria().getBounds2D());
	}

	@Override
	protected void aoColidirComLaterais() {
		inverterTragetoria();
	}
	
	public void passarPara(String jogador){
		ObjetoJogo parceiro = getCampo().getObjetosJogo().get(jogador);
		ObjetoJogo bola = getCampo().getBola();
		if(parceiro != null){
			apontarPara(parceiro.getX(), parceiro.getY());
			bola.apontarPara(parceiro.getX(), parceiro.getY());
		}
		bola.setAceleracao(4);
		bola.setVelocidade(5);
		getCampo().setJogadorComBola(null);
	}

	public void chutarGol(MovimentoBola movimentoBola) {
//		Gol golAlvo = getTime().getGolAlvo();
//		ObjetoJogo bola = getCampo().getBola();
//		bola.apontarPara(golAlvo.getLimites().getX(), golAlvo.getLimites().getY()+golAlvo.getLimites().getHeight()/2);
//		bola.setDirecao(bola.getDirecao() + Math.random() * 20 - 10);
//		bola.setAceleracao(4);
//		bola.setVelocidade(5);
		
		Gol golAlvo = getTime().getGolAlvo();
		ObjetoJogo bola = getCampo().getBola();
		bola.apontarPara(golAlvo.getLimites().getX(),
				golAlvo.getLimites().getY() + golAlvo.getLimites().getHeight() / 2);
		bola.setDirecao(bola.getDirecao() + movimentoBola.getErro());
		bola.setAceleracao(movimentoBola.getAceleracao());
		bola.setVelocidade(movimentoBola.getVelocidade());
		getCampo().setJogadorComBola(null);
	}

}
