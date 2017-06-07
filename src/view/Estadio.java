package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import grafico.Jogador;
import grafico.OuvinteAgentes;
import jogo.JogoListener;
import lombok.Getter;

@Getter
public class Estadio extends JFrame {
	
	private Campo campo;
	private JogoListener jogoListener;
	
	public Estadio() {
		Dimension tamanho = new Dimension(800,480);
		setSize(tamanho);
		setResizable(false);
		setLocationRelativeTo(null);
		campo = new Campo();
		campo.setSize(tamanho);
		add(campo);
		add(barraTestes(), BorderLayout.SOUTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		jogoListener = new OuvinteAgentes(campo);
	}
	
	private Component barraTestes() {
		JPanel barraTestes = new JPanel();
		JButton btnStart = new JButton("START");
		barraTestes.add(btnStart);
		btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				campo.start();
			}
		});
		JButton btnMoverBola = new JButton("MOVER BOLA");
		barraTestes.add(btnMoverBola);
		btnMoverBola.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				campo.moverBola();
			}
		});
		JButton btnAddJogador = new JButton("ADD JOGADOR");
		barraTestes.add(btnAddJogador);
		btnAddJogador.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				grafico.Jogador jogador = new grafico.Jogador(System.currentTimeMillis()+"", Jogador.COR_VISITANTE);
				jogador.setX((float) (Math.random()*campo.getWidth())-campo.getWidth()/2);
				jogador.setY((float) (Math.random()*campo.getHeight())-campo.getHeight()/2);
				campo.addJogador(jogador);
			}
		});
		
		JButton btnSeguirBola = new JButton("SEGUIR BOLA");
		barraTestes.add(btnSeguirBola);
		btnSeguirBola.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				grafico.Jogador jogador = new grafico.Jogador("jogador", Jogador.COR_VISITANTE);
				jogador.setX((float) (Math.random()*campo.getWidth())-campo.getWidth()/2);
				jogador.setY((float) (Math.random()*campo.getHeight())-campo.getHeight()/2);
				campo.addJogador(jogador);
				campo.jogadorSeguirBola("jogador");
			}
		});
		
		return barraTestes;
	}

	public void iniciar(){
		SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
	        	 new Estadio().setVisible(true);
	         }}
		);
	}

}
