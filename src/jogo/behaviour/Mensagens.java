package jogo.behaviour;

public interface Mensagens {

	String PEGUEI_BOLA = "peguei_bola";
	String TENHO_A_BOLA = "tenho_bola";
	String CHUTEI = "chutei";
	String PASSEI = "passei";
    String QUAL_SUA_DISTANCIA_DA_BOLA = "qual_sua_distancia_da_bola?";
	String MINHA_DISTANCIA_BOLA = "minha_distancia_bola";

    interface Gui {
		String DISTANCIA_BOLA = "distancia_bola";
		String CHEGOU_NA_AREA_ALVO = "chegou_na_grande_area_alvo";
		String BOLA_NA_AREA_DO_TIME = "bola_na_area_do_time";
		String COLIDIU_COM_BOLA = "colidiu_com_bola";
    }
}
