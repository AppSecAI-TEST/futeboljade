package grafico;

import java.awt.Rectangle;

import lombok.Data;

@Data
public class CampoAtaque {
	
	private Rectangle area;
	
	public CampoAtaque(Rectangle campo) {
		setArea(campo);
	}
	
	public Rectangle getArea(){
		if(area == null){
			System.exit(0);
		}
		return this.area;
	}

	

}
