package grafico;

import jogo.MovimentoBola;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Accessors(chain=true)
public class Jogador extends ObjetoJogo {
	
	private static final int FOLGA_TESTE_COLISAO_JOGADOR = 7;
	public static final Color COR_CASA = new Color(255, 51, 51);
	public static final Color COR_VISITANTE = new Color(0, 170, 255);
	public static final int TAMANHO_JOGADOR = 30;
	
	private Color color;
	private Time time;
	private String nome;
	private Point posicaoAtaque, posicaoDefesa;
	private int folgaTesteColisaoJogador = 0;	

	public Jogador() {
		setW(TAMANHO_JOGADOR);
		setH(TAMANHO_JOGADOR);
	}
	
	public void atualiza() {
		reposiciona();
		desenha();
		detectaColisao();
		avisaSeEstaNaGrandeArea();
		avisaAQueDistanciaEstaDaBola();
		avisaSeEstaNoAtaque();
		avisaJogadoresAFrente();
	}

	private void avisaJogadoresAFrente() {
		List<Jogador> jogadores = getTime().getJogadoresAFrente(this);
		List<String> names = jogadores.stream().map(Jogador::getNome).collect(Collectors.toList());
		getCampo().avisaJogadoresAFrente(names);
	}

	private void avisaSeEstaNoAtaque() {
		if(getGeometria().getBounds().intersects(getTime().getCampoAtaque().getArea())){
			getCampo().avisaJogadorEstaNoAtaque(this);
		}
	}

	private void avisaAQueDistanciaEstaDaBola() {
		ObjetoJogo bola = getCampo().getBola();
		double distancia = GeometriaUtil.getDistanciaAte(getX(), getY(), bola.getX(), bola.getY());
		getCampo().getListeners().forEach(l->l.jogadorEstaAXDistancia(getNome(), distancia));
	}

	private void avisaSeEstaNaGrandeArea() {
		Rectangle2D minhaArea = getGeometria().getBounds2D();
		Rectangle grandeAreaAlvo = getTime().getGrandeAreaAlvo().getLimites();
		boolean estaNaGrandeArea = grandeAreaAlvo.intersects(minhaArea);
		if(estaNaGrandeArea){
			getCampo().getListeners().forEach(l->l.jogadorEstaNaGrandeAreaAlvo(getNome()));
		}
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
		g2.drawString(getNome(), (int) getX(), (int) getY());
		g2.drawString(getDirecao() + "", (int) getX(), (int) getY() - 20);
		g2.drawString(getInfo() + "", (int) getX(), (int) getY() - 40);
		g2.drawString(getDebug() + "", (int) getX(), (int) getY() - 60);
		g2.draw(this.getGeometria().getBounds2D());
	}
	
	@Override
	protected void detectaColisao() {
		super.detectaColisao();
		if(colidiuComBola()){
			aoColidirComBola();
		}
		Jogador j;
		if((j = colidiuComJogador()) != null && getFolgaTesteColisaoJogador() <= 0){
			aoColidirComJogador(j);
			setFolgaTesteColisaoJogador(FOLGA_TESTE_COLISAO_JOGADOR);
		}
		setFolgaTesteColisaoJogador(getFolgaTesteColisaoJogador()-1);

	}
		
	private void aoColidirComJogador(Jogador j) {
		double anguloAteJogador = GeometriaUtil.getDirecaoPara(getX(), getY(), j.getX(), j.getY());
		double direcao = getDirecao();
		double diff = (direcao - anguloAteJogador);
		setDirecao(getDirecao() + (180 - diff));
	}

	private Jogador colidiuComJogador() {
		for(Jogador j : getCampo().getJogadores()){
			if(j != this && this.getGeometria().intersects(j.getGeometria().getBounds())){
				return j;
			}
		}
		return null;
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
		Gol golAlvo = getTime().getGolAlvo();
		ObjetoJogo bola = getCampo().getBola();
		bola.apontarPara(golAlvo.getLimites().getX(),
				golAlvo.getLimites().getY() + golAlvo.getLimites().getHeight() / 2);
		bola.setDirecao(bola.getDirecao() + movimentoBola.getErro());
		bola.setAceleracao(movimentoBola.getAceleracao());
		bola.setVelocidade(movimentoBola.getVelocidade());
		getCampo().setJogadorComBola(null);
	}

	public void atacar() {
		apontarPara(getPosicaoAtaque().getX(), getPosicaoAtaque().getY());
		setVelocidade(3);
		setAceleracao(1);
	}
	
	public void defender() {
		apontarPara(getPosicaoDefesa().getX(), getPosicaoDefesa().getY());
		setVelocidade(3);
		setAceleracao(1);
	}

}
