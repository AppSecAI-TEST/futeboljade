package grafico;

import java.awt.Rectangle;

import lombok.Data;

@Data
public class CampoMetade {
	
	private Rectangle area;
	
	public CampoMetade(Rectangle campo) {
		setArea(campo);
	}
	
	public Rectangle getArea(){
		if(area == null){
			System.exit(0);
		}
		return this.area;
	}

	

}
