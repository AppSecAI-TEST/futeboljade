package jogo.behaviour;

public interface Mensagens {

	String PEGUEI_BOLA = "peguei_bola";
	String TENHO_A_BOLA = "tenho_bola";
	String CHUTEI = "chutei";
	String PASSEI = "passei";
	String QUAL_SUA_DISTANCIA_DA_BOLA = "qual_sua_distancia_da_bola?";
	String MINHA_DISTANCIA_BOLA = "minha_distancia_bola";
	String VOU_BOTAR_BOLA_NO_CENTRO = "vol_colocar_bola_no_centro";
	String ESTA_COLOCANDO_BOLA_NO_CENTRO = "esta_colocando_bola_no_centro?";
	String SIM = "sim";

    interface Gui {
		String DISTANCIA_BOLA = "distancia_bola";
		String BOLA_NA_AREA_DO_TIME = "bola_na_area_do_time";
		String COLIDIU_COM_BOLA = "colidiu_com_bola";
        String GOL = "GOL";
    }
}
