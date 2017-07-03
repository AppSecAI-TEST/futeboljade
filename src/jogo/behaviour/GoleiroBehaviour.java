package jogo.behaviour;

import jade.core.Agent;

public class GoleiroBehaviour extends JogadorBehaviour {

	private boolean bolaNaArea;

	public GoleiroBehaviour(Agent agent) {
		super(agent);
	}

	@Override
	public void executaPassoJogo(InformacoesPassoJogo informacoesPassoJogo) {
		super.executaPassoJogo(informacoesPassoJogo);
		if(bolaChegouNaArea(informacoesPassoJogo)){
			bolaNaArea = true;
		} else if(bolaSaiuDaArea(informacoesPassoJogo)){
			bolaNaArea = false;
		}
	}

	private boolean bolaSaiuDaArea(InformacoesPassoJogo informacoesPassoJogo) {
		String bolaSaiuDaArea = "saiu_da_area:" + getJogador().getTime();
		return bolaSaiuDaArea.equals(informacoesPassoJogo.getMensagemDaInterface());
	}

	private boolean bolaChegouNaArea(InformacoesPassoJogo informacoesPassoJogo) {
		String bolaChegouNaArea = "chegou_na_grande_area_alvo:" + getJogador().getTime();
		return bolaChegouNaArea.equals( informacoesPassoJogo.getMensagemDaInterface());
	}

	@Override
	protected void jogaTimeComBola() {
		if(!bolaNaArea)
			getJogador().atacar();
	}
	
	@Override
	protected void jogaComBola() {
		getJogador().jogaComBola();
	}

}
