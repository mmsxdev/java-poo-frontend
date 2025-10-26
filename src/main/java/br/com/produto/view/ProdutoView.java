package br.com.produto.view;

import br.com.produto.enums.TipoProduto;
import br.com.produto.model.ProdutoModel;
import br.com.produto.service.ProdutoService;
import br.com.produto.table.ProdutoTableModel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ProdutoView extends JFrame {

    // --- Componentes da UI ---
    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtReferencia;
    private JTextField txtFornecedor;
    private JTextField txtMarca;
    private JComboBox<TipoProduto> cmbCategoria;

    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnExcluir;

    private JTable tblProdutos;

    // --- Lógica e Dados ---
    private final ProdutoService produtoService;
    private final ProdutoTableModel produtoTableModel;

    public ProdutoView() {
        this.produtoService = new ProdutoService();
        this.produtoTableModel = new ProdutoTableModel();

        setTitle("Cadastro de Produtos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // DISPOSE para não fechar a aplicação inteira
        setLocationRelativeTo(null);

        initComponents();
        initLayout();
        initActions();

        carregarProdutos();
    }

    private void initComponents() {
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(230, 230, 230));

        txtNome = new JTextField(30);
        txtReferencia = new JTextField(15);
        txtFornecedor = new JTextField(20);
        txtMarca = new JTextField(15);

        cmbCategoria = new JComboBox<>(TipoProduto.values());
        cmbCategoria.setRenderer(new TipoProdutoRenderer());

        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnExcluir = new JButton("Excluir");

        tblProdutos = new JTable(produtoTableModel);
        tblProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void initLayout() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.1; formPanel.add(txtId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.9; gbc.gridwidth = 3; formPanel.add(txtNome, gbc);
        gbc.gridwidth = 1;

        // Linha 1
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Referência:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.3; formPanel.add(txtReferencia, gbc);

        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(new JLabel("Fornecedor:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.7; formPanel.add(txtFornecedor, gbc);

        // Linha 2
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Marca:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.3; formPanel.add(txtMarca, gbc);

        gbc.gridx = 2; gbc.gridy = 2; formPanel.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 0.7; formPanel.add(cmbCategoria, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnNovo);
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnExcluir);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.add(formPanel, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(tblProdutos), BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initActions() {
        btnNovo.addActionListener(e -> limparFormulario());
        btnSalvar.addActionListener(e -> salvarProduto());
        btnExcluir.addActionListener(e -> excluirProduto());

        tblProdutos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblProdutos.getSelectedRow();
                if (selectedRow != -1) {
                    ProdutoModel produtoSelecionado = produtoTableModel.getProdutoAt(selectedRow);
                    preencherFormulario(produtoSelecionado);
                }
            }
        });
    }

    private void carregarProdutos() {
        try {
            produtoTableModel.setProdutos(produtoService.buscarTodosProdutos());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar produtos do servidor: " + e.getMessage(),
                    "Erro de Conexão",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        txtId.setText("");
        txtNome.setText("");
        txtReferencia.setText("");
        txtFornecedor.setText("");
        txtMarca.setText("");
        cmbCategoria.setSelectedIndex(0);
        tblProdutos.clearSelection();
        txtNome.requestFocus();
    }

    private void preencherFormulario(ProdutoModel produto) {
        txtId.setText(produto.getId() != null ? produto.getId().toString() : "");
        txtNome.setText(produto.getNome());
        txtReferencia.setText(produto.getReferencia());
        txtFornecedor.setText(produto.getFornecedor());
        txtMarca.setText(produto.getMarca());
        cmbCategoria.setSelectedItem(produto.getCategoria());
    }

    private void salvarProduto() {
        if (txtNome.getText().trim().isEmpty() || txtReferencia.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e Referência são obrigatórios.", "Campos Inválidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ProdutoModel produto = new ProdutoModel();
        String idText = txtId.getText();
        if (idText != null && !idText.isEmpty()) {
            produto.setId(Long.parseLong(idText));
        }

        produto.setNome(txtNome.getText().trim());
        produto.setReferencia(txtReferencia.getText().trim());
        produto.setFornecedor(txtFornecedor.getText().trim());
        produto.setMarca(txtMarca.getText().trim());
        produto.setCategoria((TipoProduto) cmbCategoria.getSelectedItem());

        try {
            produtoService.salvarProduto(produto);
            JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            carregarProdutos();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar produto: " + e.getMessage(),
                    "Erro de Salvamento",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirProduto() {
        int selectedRow = tblProdutos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para excluir.", "Nenhuma Seleção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ProdutoModel produtoSelecionado = produtoTableModel.getProdutoAt(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o produto '" + produtoSelecionado.getNome() + "'?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                produtoService.deletarProduto(produtoSelecionado.getId());
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                carregarProdutos();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir produto: " + e.getMessage(),
                        "Erro de Exclusão",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Ponto de entrada para testar esta tela individualmente.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            new ProdutoView().setVisible(true);
        });
    }
}