package view;

import grafico.Jogador;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Getter
@Setter
public class Estadio extends JFrame {
	
	private Campo campo;
	
	private Listener listener;
	
	public Estadio() {
		Dimension tamanho = new Dimension(1000,750);
		setSize(tamanho);
		setResizable(false);
		setLocationRelativeTo(null);
		campo = new Campo();
		campo.setSize(tamanho);
		add(campo);
		add(barraTestes(), BorderLayout.SOUTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				super.windowOpened(e);
				campo.start();
				campo.addBola();
				if(listener != null) listener.iniciou();
			}
		});
	}
	
	private Component barraTestes() {
		JPanel barraTestes = new JPanel();
		barraTestes.setPreferredSize(new Dimension(400,100));
		
		JButton btnMoverBola = new JButton("MOVER BOLA");
		barraTestes.add(btnMoverBola);
		btnMoverBola.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				campo.moverBola();
			}
		});
		
		JButton btnAddJogador = new JButton("ADD JOGADOR CASA");
		barraTestes.add(btnAddJogador);
		btnAddJogador.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				campo.addJogador(System.currentTimeMillis()+"", "CASA");
			}
		});
		
		JButton btnAddJogadorVisitante = new JButton("ADD JOGADOR VISITANTE");
		barraTestes.add(btnAddJogadorVisitante);
		btnAddJogadorVisitante.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nome = System.currentTimeMillis()+"";
				campo.addJogador(nome, "VISITANTE");
			}
		});
		
		JButton btnAddJogadorComBola = new JButton("ADD JOGADOR COM BOLA");
		barraTestes.add(btnAddJogadorComBola);
		btnAddJogadorComBola.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				campo.addJogador("jogadorComBola", "VISITANTE");
				Jogador jogador = campo.getJogador("jogadorComBola");
				campo.setJogadorComBola(jogador);
				jogador.setVelocidade(3);
				jogador.setAceleracao(1);
				jogador.setDirecao(45);
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
				campo.addJogador("JOGADOR", "CASA");
				campo.jogadorSeguirBola("JOGADOR");
			}
		});
		
		JButton btnChutar = new JButton("CHUTAR");
		barraTestes.add(btnChutar);
		btnChutar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				campo.jogadorComBolaChutarGol();
			}
		});
		
		JButton btnAddParceiro = new JButton("ADD PARCEIRO");
		barraTestes.add(btnAddParceiro);
		btnAddParceiro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				campo.addJogador("PARCEIRO", "CASA");
			}
		});
		
		JButton btnPassar = new JButton("PASSAR");
		barraTestes.add(btnPassar);
		btnPassar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				campo.jogadorComBolaPassarPara("PARCEIRO");
			}
		});
		
		JButton btnAvancarAtaque = new JButton("CASA AVANÇAR");
		barraTestes.add(btnAvancarAtaque);
		btnAvancarAtaque.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				campo.timeCasaAvancarAtaque();
			}
		});
		
		JButton addGoleiroCasa = new JButton("ADD GOLEIRO CASA");
		barraTestes.add(addGoleiroCasa);
		addGoleiroCasa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				campo.addGoleiroCasa("casa_goleiro");
			}
		});
		
		JButton addGoleiroVisitante = new JButton("ADD GOLEIRO VISITANTE");
		barraTestes.add(addGoleiroVisitante);
		addGoleiroVisitante.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				campo.addGoleiroVisitante("visitante_goleiro");
			}
		});
		
		JButton moveBolaCentro = new JButton("MOVE BOLA CENTRO");
		barraTestes.add(moveBolaCentro);
		moveBolaCentro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				campo.moveBolaCentro();
				
			}
		});
		
		return barraTestes;
	}

	public void iniciar(){
		SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
				setVisible(true);
	         }}
		);
	}
	
	public static void main(String[] args) {
		new Estadio().iniciar();
	}
	
	public interface Listener {
		void iniciou();
	}

}
