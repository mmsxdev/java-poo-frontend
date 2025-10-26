package br.com.preco.view;

import br.com.preco.enums.TipoEstoque;
import br.com.preco.model.PrecoModel;
import br.com.preco.service.PrecoService;
import br.com.preco.table.PrecoTableModel;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class PrecoView extends JFrame {

    // --- Componentes da UI ---
    private JTextField txtId;
    private JComboBox<TipoEstoque> cmbTipoEstoque;
    private JTextField txtValor;
    private JFormattedTextField txtDataVigencia;

    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnExcluir;

    private JTable tblPrecos;

    // --- Lógica e Dados ---
    private final PrecoService precoService;
    private final PrecoTableModel precoTableModel;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PrecoView() {
        this.precoService = new PrecoService();
        this.precoTableModel = new PrecoTableModel();

        setTitle("Cadastro de Preços de Combustíveis");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        initLayout();
        initActions();

        carregarPrecos();
    }

    private void initComponents() {
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(230, 230, 230));

        cmbTipoEstoque = new JComboBox<>(TipoEstoque.values());
        cmbTipoEstoque.setRenderer(new TipoEstoqueRenderer());

        txtValor = new JTextField(10);

        try {
            MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
            dateFormatter.setPlaceholderCharacter('_');
            txtDataVigencia = new JFormattedTextField(dateFormatter);
        } catch (ParseException e) {
            txtDataVigencia = new JFormattedTextField();
            e.printStackTrace();
        }

        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnExcluir = new JButton("Excluir");

        tblPrecos = new JTable(precoTableModel);
        tblPrecos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void initLayout() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Preço"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.2; formPanel.add(txtId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(new JLabel("Tipo de Combustível:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.8; formPanel.add(cmbTipoEstoque, gbc);

        // Linha 1
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Valor (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.2; formPanel.add(txtValor, gbc);

        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(new JLabel("Data de Vigência:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.8; formPanel.add(txtDataVigencia, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnNovo);
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnExcluir);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.add(formPanel, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(tblPrecos), BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initActions() {
        btnNovo.addActionListener(e -> limparFormulario());
        btnSalvar.addActionListener(e -> salvarPreco());
        btnExcluir.addActionListener(e -> excluirPreco());

        tblPrecos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblPrecos.getSelectedRow();
                if (selectedRow != -1) {
                    PrecoModel precoSelecionado = precoTableModel.getPrecoAt(selectedRow);
                    preencherFormulario(precoSelecionado);
                }
            }
        });
    }

    private void carregarPrecos() {
        try {
            precoTableModel.setPrecos(precoService.buscarTodosPrecos());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar preços do servidor: " + e.getMessage(),
                    "Erro de Conexão",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        txtId.setText("");
        cmbTipoEstoque.setSelectedIndex(0);
        txtValor.setText("");
        txtDataVigencia.setValue(null);
        txtDataVigencia.setText("");
        tblPrecos.clearSelection();
        cmbTipoEstoque.requestFocus();
    }

    private void preencherFormulario(PrecoModel preco) {
        txtId.setText(preco.getId() != null ? preco.getId().toString() : "");
        cmbTipoEstoque.setSelectedItem(preco.getTipoEstoque());
        txtValor.setText(preco.getValor() != null ? preco.getValor().toPlainString() : "");
        txtDataVigencia.setText(preco.getDataVigencia() != null ? preco.getDataVigencia().format(DATE_FORMATTER) : "");
    }

    private void salvarPreco() {
        if (txtValor.getText().trim().isEmpty() || txtDataVigencia.getText().replace("_", "").trim().length() < 8) {
            JOptionPane.showMessageDialog(this, "Valor e Data de Vigência são obrigatórios.", "Campos Inválidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PrecoModel preco = new PrecoModel();
        String idText = txtId.getText();
        if (idText != null && !idText.isEmpty()) {
            preco.setId(Long.parseLong(idText));
        }

        try {
            // Substitui vírgula por ponto para garantir o parse correto do BigDecimal
            String valorText = txtValor.getText().trim().replace(",", ".");
            preco.setValor(new BigDecimal(valorText));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido. Use apenas números e ponto/vírgula decimal.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String dateText = txtDataVigencia.getText().replaceAll("[^\\d/]", "");
            preco.setDataVigencia(LocalDate.parse(dateText, DATE_FORMATTER));
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        preco.setTipoEstoque((TipoEstoque) cmbTipoEstoque.getSelectedItem());

        try {
            precoService.salvarPreco(preco);
            JOptionPane.showMessageDialog(this, "Preço salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            carregarPrecos();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar preço: " + e.getMessage(),
                    "Erro de Salvamento",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirPreco() {
        int selectedRow = tblPrecos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um preço na tabela para excluir.", "Nenhuma Seleção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PrecoModel precoSelecionado = precoTableModel.getPrecoAt(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o preço para '" + precoSelecionado.getTipoEstoque().getDescricao() + "'?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                precoService.deletarPreco(precoSelecionado.getId());
                JOptionPane.showMessageDialog(this, "Preço excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                carregarPrecos();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir preço: " + e.getMessage(),
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

            new PrecoView().setVisible(true);
        });
    }
}