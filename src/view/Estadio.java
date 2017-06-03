package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Estadio extends JFrame {
	
	private Campo campo;
	
	public Estadio() {
		setSize(800,450);
		setLocationRelativeTo(null);
		campo = new Campo();
		add(campo);
		add(barraTestes(), BorderLayout.SOUTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private Component barraTestes() {
		JPanel barraTestes = new JPanel();
		JButton btnMoverBola = new JButton("MOVER BOLA");
		barraTestes.add(btnMoverBola);
		btnMoverBola.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				campo.gameLoop();
				campo.moverBola();
			}
		});
		
		return barraTestes;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
	        	 new Estadio().setVisible(true);
	         }}
		);
	}

}
