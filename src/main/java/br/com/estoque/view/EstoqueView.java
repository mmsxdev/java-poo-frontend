package br.com.estoque.view;

import br.com.estoque.enums.TipoEstoque;
import br.com.estoque.model.EstoqueModel;
import br.com.estoque.service.EstoqueService;
import br.com.estoque.table.EstoqueTableModel;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EstoqueView extends JFrame {

    // --- Componentes da UI ---
    private JTextField txtId;
    private JComboBox<TipoEstoque> cmbTipoEstoque;
    private JTextField txtQuantidade;
    private JTextField txtLocalTanque;
    private JTextField txtLocalEndereco;
    private JTextField txtLoteFabricacao;
    private JFormattedTextField txtDataValidade;

    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnExcluir;

    private JTable tblEstoques;

    // --- Lógica e Dados ---
    private final EstoqueService estoqueService;
    private final EstoqueTableModel estoqueTableModel;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public EstoqueView() {
        this.estoqueService = new EstoqueService();
        this.estoqueTableModel = new EstoqueTableModel();

        setTitle("Controle de Estoque de Combustíveis");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        initLayout();
        initActions();

        carregarEstoques();
    }

    private void initComponents() {
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(230, 230, 230));

        cmbTipoEstoque = new JComboBox<>(TipoEstoque.values());
        cmbTipoEstoque.setRenderer(new TipoEstoqueRenderer());

        txtQuantidade = new JTextField(10);
        txtLocalTanque = new JTextField(15);
        txtLocalEndereco = new JTextField(25);
        txtLoteFabricacao = new JTextField(15);

        try {
            MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
            dateFormatter.setPlaceholderCharacter('_');
            txtDataValidade = new JFormattedTextField(dateFormatter);
        } catch (ParseException e) {
            txtDataValidade = new JFormattedTextField();
            e.printStackTrace();
        }

        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnExcluir = new JButton("Excluir");

        tblEstoques = new JTable(estoqueTableModel);
        tblEstoques.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void initLayout() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Estoque"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.1; formPanel.add(txtId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(new JLabel("Tipo de Combustível:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.9; gbc.gridwidth = 3; formPanel.add(cmbTipoEstoque, gbc);
        gbc.gridwidth = 1;

        // Linha 1
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Quantidade (L):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.2; formPanel.add(txtQuantidade, gbc);

        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(new JLabel("Local (Tanque):"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.8; formPanel.add(txtLocalTanque, gbc);

        // Linha 2
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Endereço do Tanque:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; gbc.gridwidth = 3; formPanel.add(txtLocalEndereco, gbc);
        gbc.gridwidth = 1;

        // Linha 3
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Lote:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.2; formPanel.add(txtLoteFabricacao, gbc);

        gbc.gridx = 2; gbc.gridy = 3; formPanel.add(new JLabel("Data de Validade:"), gbc);
        gbc.gridx = 3; gbc.gridy = 3; gbc.weightx = 0.8; formPanel.add(txtDataValidade, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnNovo);
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnExcluir);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.add(formPanel, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(tblEstoques), BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initActions() {
        btnNovo.addActionListener(e -> limparFormulario());
        btnSalvar.addActionListener(e -> salvarEstoque());
        btnExcluir.addActionListener(e -> excluirEstoque());

        tblEstoques.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblEstoques.getSelectedRow();
                if (selectedRow != -1) {
                    EstoqueModel estoqueSelecionado = estoqueTableModel.getEstoqueAt(selectedRow);
                    preencherFormulario(estoqueSelecionado);
                }
            }
        });
    }

    private void carregarEstoques() {
        try {
            estoqueTableModel.setEstoques(estoqueService.buscarTodosEstoques());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar estoques do servidor: " + e.getMessage(),
                    "Erro de Conexão",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        txtId.setText("");
        cmbTipoEstoque.setSelectedIndex(0);
        txtQuantidade.setText("");
        txtLocalTanque.setText("");
        txtLocalEndereco.setText("");
        txtLoteFabricacao.setText("");
        txtDataValidade.setValue(null);
        txtDataValidade.setText("");
        tblEstoques.clearSelection();
        cmbTipoEstoque.requestFocus();
    }

    private void preencherFormulario(EstoqueModel estoque) {
        txtId.setText(estoque.getId() != null ? estoque.getId().toString() : "");
        cmbTipoEstoque.setSelectedItem(estoque.getTipoEstoque());
        txtQuantidade.setText(estoque.getQuantidade() != null ? estoque.getQuantidade().toPlainString() : "");
        txtLocalTanque.setText(estoque.getLocalTanque());
        txtLocalEndereco.setText(estoque.getLocalEndereco());
        txtLoteFabricacao.setText(estoque.getLoteFabricacao());
        txtDataValidade.setText(estoque.getDataValidade() != null ? estoque.getDataValidade().format(DATE_FORMATTER) : "");
    }

    private void salvarEstoque() {
        if (txtQuantidade.getText().trim().isEmpty() || txtLocalTanque.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Quantidade e Local (Tanque) são obrigatórios.", "Campos Inválidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        EstoqueModel estoque = new EstoqueModel();
        String idText = txtId.getText();
        if (idText != null && !idText.isEmpty()) {
            estoque.setId(Long.parseLong(idText));
        }

        try {
            String valorText = txtQuantidade.getText().trim().replace(",", ".");
            estoque.setQuantidade(new BigDecimal(valorText));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida. Use apenas números e ponto/vírgula decimal.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String dateText = txtDataValidade.getText().replaceAll("[^\\d/]", "");
            if (!dateText.replace("/", "").trim().isEmpty()) {
                estoque.setDataValidade(LocalDate.parse(dateText, DATE_FORMATTER));
            } else {
                JOptionPane.showMessageDialog(this, "Data de Validade é obrigatória.", "Campo Inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        estoque.setTipoEstoque((TipoEstoque) cmbTipoEstoque.getSelectedItem());
        estoque.setLocalTanque(txtLocalTanque.getText().trim());
        estoque.setLocalEndereco(txtLocalEndereco.getText().trim());
        estoque.setLoteFabricacao(txtLoteFabricacao.getText().trim());

        try {
            estoqueService.salvarEstoque(estoque);
            JOptionPane.showMessageDialog(this, "Estoque salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            carregarEstoques();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar estoque: " + e.getMessage(),
                    "Erro de Salvamento",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirEstoque() {
        int selectedRow = tblEstoques.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um item do estoque na tabela para excluir.", "Nenhuma Seleção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        EstoqueModel estoqueSelecionado = estoqueTableModel.getEstoqueAt(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o registro de estoque para '" + estoqueSelecionado.getTipoEstoque().getDescricao() + "'?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                estoqueService.deletarEstoque(estoqueSelecionado.getId());
                JOptionPane.showMessageDialog(this, "Registro de estoque excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                carregarEstoques();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir registro de estoque: " + e.getMessage(),
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

            new EstoqueView().setVisible(true);
        });
    }
}