package registro;

import java.awt.EventQueue;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import net.proteanit.sql.DbUtils;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TelaRegistro {

	private JFrame frame;
	private JTextField txtnome;
	private JTextField txtquantidade;
	private JTextField txtbarras;
	private JTable table;
	private JTextField txtid;

	
	 /* Inicializar aplicação. */
	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaRegistro window = new TelaRegistro();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


		/* Criar aplicação. */
	
	public TelaRegistro() {
		initialize();
		Connect();
		table_load();
	}
	
	
	Connection conTabela;
	PreparedStatement prStats;
	ResultSet result;
	
	public void Connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conTabela = DriverManager.getConnection("jdbc:mysql://localhost/javaaps", "root","");
		}
		catch (ClassNotFoundException ex) {
			
		}
		catch (SQLException ex) {
			
		}
		
	}
	
	public void table_load() {
		try {
			prStats = conTabela.prepareStatement("SELECT * FROM tabelaitens");
			result = prStats.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(result));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	} 
	 
		/* Inicializa os conteúdos da janela. */
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 832, 402);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("CADASTRO DE ITENS");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblNewLabel.setBounds(293, 11, 250, 51);
		frame.getContentPane().add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Registro", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 73, 324, 180);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblQuantidade = new JLabel("QUANTIDADE");
		lblQuantidade.setBounds(10, 67, 76, 14);
		panel.add(lblQuantidade);
		
		JLabel lblNome = new JLabel("NOME");
		lblNome.setBounds(10, 23, 76, 14);
		panel.add(lblNome);
		
		JLabel lblBarras = new JLabel("C. DE BARRAS");
		lblBarras.setBounds(10, 115, 76, 14);
		panel.add(lblBarras);
		
		txtnome = new JTextField();
		txtnome.setBounds(112, 20, 192, 20);
		panel.add(txtnome);
		txtnome.setColumns(10);
		
		txtquantidade = new JTextField();
		txtquantidade.setBounds(112, 64, 192, 20);
		panel.add(txtquantidade);
		txtquantidade.setColumns(10);
		
		txtbarras = new JTextField();
		txtbarras.setBounds(112, 112, 192, 20);
		panel.add(txtbarras);
		txtbarras.setColumns(10);
		
		/* Botão salvar */
		
		JButton btnAdicionar = new JButton("SALVAR");
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nome, quantidade, barras;
					
					nome = txtnome.getText();
					quantidade = txtquantidade.getText();
					barras = txtbarras.getText();
						
						try {
							prStats = conTabela.prepareStatement("INSERT INTO tabelaitens(nome, quantidade, codbarras)values(?,?,?)");
							prStats.setString(1, nome);
							prStats.setString(2, quantidade);
							prStats.setString(3, barras);
							prStats.executeUpdate();
							JOptionPane.showMessageDialog(null, "Dados salvos");
							table_load();
							txtnome.setText("");
							txtquantidade.setText("");
							txtbarras.setText("");
							
						}
						
						catch (SQLException e1) {
							e1.printStackTrace();
						}
			}
		});
		btnAdicionar.setBounds(10, 264, 104, 23);
		frame.getContentPane().add(btnAdicionar);
		
		/* Botão limpar */
		
		JButton btnLimpar = new JButton("LIMPAR");
		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				txtnome.setText("");
				txtquantidade.setText("");
				txtbarras.setText("");
				txtid.setText("");
				
			}
		});
		btnLimpar.setBounds(124, 264, 97, 23);
		frame.getContentPane().add(btnLimpar);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(355, 80, 447, 173);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Pesquisa", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(429, 268, 314, 53);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblID = new JLabel("ID");
		lblID.setHorizontalAlignment(SwingConstants.CENTER);
		lblID.setBounds(10, 26, 57, 14);
		panel_1.add(lblID);
		
		/* Campo pesquisa */
		
		txtid = new JTextField();
		txtid.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					String id = txtid.getText();
						
					prStats = conTabela.prepareStatement("SELECT nome, quantidade, codbarras FROM tabelaitens WHERE id = ?");
					prStats.setString(1, id);
					ResultSet rs = prStats.executeQuery();
							
						   if(rs.next() == true) {
								String nome = rs.getString(1);
								String quantidade = rs.getString(2);
								String codbarras = rs.getString(3);
								
								txtnome.setText(nome);
								txtquantidade.setText(quantidade);
								txtbarras.setText(codbarras);
							}
							
							else {
								txtnome.setText("");
								txtquantidade.setText("");
								txtbarras.setText("");
							}
				}
				
				catch (SQLException ex){
					
				}									
			}
		});
		txtid.setBounds(112, 20, 192, 20);
		panel_1.add(txtid);
		txtid.setColumns(10);
		
		/* Botão atualizar */
		
		JButton btnAtualizar = new JButton("ATUALIZAR");
		btnAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nome, quantidade, barras, id;
				nome = txtnome.getText();
				quantidade = txtquantidade.getText();
				barras = txtbarras.getText();
				id = txtid.getText();
					
					try {
						prStats = conTabela.prepareStatement("UPDATE tabelaitens SET nome= ?, quantidade= ?, codbarras= ? WHERE id= ?");
						prStats.setString(1, nome);
						prStats.setString(2, quantidade);
						prStats.setString(3, barras);
						prStats.setString(4, id);
						prStats.executeUpdate();
						JOptionPane.showMessageDialog(null, "Dados atualizados");
						table_load();
						txtnome.setText("");
						txtquantidade.setText("");
						txtbarras.setText("");
						
					}
					
					catch (SQLException e1) {
						e1.printStackTrace();
					}
			}
		});
		btnAtualizar.setBounds(230, 264, 104, 23);
		frame.getContentPane().add(btnAtualizar);
		
		/* Botão apagar */
		
		JButton btnApagar = new JButton("APAGAR");
		btnApagar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id;
				
				id = txtid.getText();
					
					try {
						prStats = conTabela.prepareStatement("DELETE FROM tabelaitens WHERE id= ?");
						prStats.setString(1, id);
						prStats.executeUpdate();
						JOptionPane.showMessageDialog(null, "Dados apagados");
						table_load();
						txtnome.setText("");
						txtquantidade.setText("");
						txtbarras.setText("");
						
					}
					
					catch (SQLException e1) {
						e1.printStackTrace();
					}
			}
		});
		btnApagar.setBounds(124, 298, 97, 23);
		frame.getContentPane().add(btnApagar);
	}
}
