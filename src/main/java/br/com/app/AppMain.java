package br.com.app;

import br.com.acesso.view.AcessoView;
import br.com.contato.view.ContatoView;
import br.com.custo.view.CustoView;
import br.com.estoque.view.EstoqueView;
import br.com.pessoa.view.PessoaView;
import br.com.preco.view.PrecoView;
import br.com.produto.view.ProdutoView;

import javax.swing.*;
import java.awt.*;

public class AppMain extends JFrame {

    public AppMain() {
        setTitle("PDV Posto de Combustível - Sistema de Gestão");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Cria a Barra de Menu ---
        JMenuBar menuBar = new JMenuBar();

        // --- Menu Cadastros ---
        JMenu menuCadastros = new JMenu("Cadastros");
        menuBar.add(menuCadastros);

        // --- Itens do Menu Cadastros ---
        JMenuItem itemPessoas = new JMenuItem("Pessoas");
        itemPessoas.addActionListener(e -> new PessoaView().setVisible(true));
        menuCadastros.add(itemPessoas);

        JMenuItem itemProdutos = new JMenuItem("Produtos");
        itemProdutos.addActionListener(e -> new ProdutoView().setVisible(true));
        menuCadastros.add(itemProdutos);

        JMenuItem itemPrecos = new JMenuItem("Preços de Combustíveis");
        itemPrecos.addActionListener(e -> new PrecoView().setVisible(true));
        menuCadastros.add(itemPrecos);

        JMenuItem itemEstoque = new JMenuItem("Estoque de Combustíveis");
        itemEstoque.addActionListener(e -> new EstoqueView().setVisible(true));
        menuCadastros.add(itemEstoque);

        JMenuItem itemCustos = new JMenuItem("Custos");
        itemCustos.addActionListener(e -> new CustoView().setVisible(true));
        menuCadastros.add(itemCustos);

        // Separador
        menuCadastros.addSeparator();

        JMenuItem itemContatos = new JMenuItem("Contatos (Isolado)");
        itemContatos.addActionListener(e -> new ContatoView().setVisible(true));
        menuCadastros.add(itemContatos);

        JMenuItem itemAcessos = new JMenuItem("Acessos de Usuário");
        itemAcessos.addActionListener(e -> new AcessoView().setVisible(true));
        menuCadastros.add(itemAcessos);

        // Adiciona a barra de menu ao frame
        setJMenuBar(menuBar);

        // --- Painel de Boas-Vindas ---
        JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'><h1>Bem-vindo ao Sistema de Gestão</h1><p>Selecione uma opção no menu 'Cadastros' para começar.</p></div></html>", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        add(welcomeLabel, BorderLayout.CENTER);
    }

    /**
     * Ponto de entrada principal da aplicação.
     */
    public static void main(String[] args) {
        // Garante que a UI seja criada na Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Tenta aplicar um Look and Feel mais moderno
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                // Se o Nimbus não estiver disponível, usa o padrão do sistema
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            new AppMain().setVisible(true);
        });
    }
}