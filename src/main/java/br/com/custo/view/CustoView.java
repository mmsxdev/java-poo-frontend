package br.com.custo.view;

import br.com.custo.enums.TipoCusto;
import br.com.custo.model.CustoModel;
import br.com.custo.service.CustoService;
import br.com.custo.table.CustoTableModel;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustoView extends JFrame {

    // --- Componentes da UI ---
    private JTextField txtId;
    private JComboBox<TipoCusto> cmbTipoCusto;
    private JTextField txtImposto;
    private JTextField txtCustoVariavel;
    private JTextField txtCustoFixo;
    private JTextField txtMargemLucro;
    private JFormattedTextField txtDataProcessamento;

    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnExcluir;

    private JTable tblCustos;

    // --- Lógica e Dados ---
    private final CustoService custoService;
    private final CustoTableModel custoTableModel;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CustoView() {
        this.custoService = new CustoService();
        this.custoTableModel = new CustoTableModel();

        setTitle("Cadastro de Custos");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        initLayout();
        initActions();

        carregarCustos();
    }

    private void initComponents() {
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(230, 230, 230));

        cmbTipoCusto = new JComboBox<>(TipoCusto.values());
        cmbTipoCusto.setRenderer(new TipoCustoRenderer());

        txtImposto = new JTextField(10);
        txtCustoVariavel = new JTextField(10);
        txtCustoFixo = new JTextField(10);
        txtMargemLucro = new JTextField(10);

        try {
            MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
            dateFormatter.setPlaceholderCharacter('_');
            txtDataProcessamento = new JFormattedTextField(dateFormatter);
        } catch (ParseException e) {
            txtDataProcessamento = new JFormattedTextField();
            e.printStackTrace();
        }

        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnExcluir = new JButton("Excluir");

        tblCustos = new JTable(custoTableModel);
        tblCustos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void initLayout() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Custo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.1; formPanel.add(txtId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(new JLabel("Tipo de Custo:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.9; gbc.gridwidth = 3; formPanel.add(cmbTipoCusto, gbc);
        gbc.gridwidth = 1;

        // Linha 1
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Imposto (%):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.2; formPanel.add(txtImposto, gbc);

        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(new JLabel("Custo Fixo (R$):"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.3; formPanel.add(txtCustoFixo, gbc);

        // Linha 2
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Custo Variável (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.2; formPanel.add(txtCustoVariavel, gbc);

        gbc.gridx = 2; gbc.gridy = 2; formPanel.add(new JLabel("Margem Lucro (%):"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 0.3; formPanel.add(txtMargemLucro, gbc);

        // Linha 3
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Data Processamento:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.2; formPanel.add(txtDataProcessamento, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnNovo);
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnExcluir);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.add(formPanel, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(tblCustos), BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initActions() {
        btnNovo.addActionListener(e -> limparFormulario());
        btnSalvar.addActionListener(e -> salvarCusto());
        btnExcluir.addActionListener(e -> excluirCusto());

        tblCustos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblCustos.getSelectedRow();
                if (selectedRow != -1) {
                    CustoModel custoSelecionado = custoTableModel.getCustoAt(selectedRow);
                    preencherFormulario(custoSelecionado);
                }
            }
        });
    }

    private void carregarCustos() {
        try {
            custoTableModel.setCustos(custoService.buscarTodosCustos());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar custos do servidor: " + e.getMessage(),
                    "Erro de Conexão",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        txtId.setText("");
        cmbTipoCusto.setSelectedIndex(0);
        txtImposto.setText("");
        txtCustoVariavel.setText("");
        txtCustoFixo.setText("");
        txtMargemLucro.setText("");
        txtDataProcessamento.setValue(null);
        txtDataProcessamento.setText("");
        tblCustos.clearSelection();
        cmbTipoCusto.requestFocus();
    }

    private void preencherFormulario(CustoModel custo) {
        txtId.setText(custo.getId() != null ? custo.getId().toString() : "");
        cmbTipoCusto.setSelectedItem(custo.getTipoCusto());
        txtImposto.setText(custo.getImposto() != null ? custo.getImposto().toPlainString() : "");
        txtCustoVariavel.setText(custo.getCustoVariavel() != null ? custo.getCustoVariavel().toPlainString() : "");
        txtCustoFixo.setText(custo.getCustoFixo() != null ? custo.getCustoFixo().toPlainString() : "");
        txtMargemLucro.setText(custo.getMargemLucro() != null ? custo.getMargemLucro().toPlainString() : "");
        txtDataProcessamento.setText(custo.getDataProcessamento() != null ? custo.getDataProcessamento().format(DATE_FORMATTER) : "");
    }

    private void salvarCusto() {
        // Validação simples
        if (txtImposto.getText().trim().isEmpty() || txtCustoFixo.getText().trim().isEmpty() || txtDataProcessamento.getText().replace("_", "").trim().length() < 8) {
            JOptionPane.showMessageDialog(this, "Imposto, Custo Fixo e Data de Processamento são obrigatórios.", "Campos Inválidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CustoModel custo = new CustoModel();
        String idText = txtId.getText();
        if (idText != null && !idText.isEmpty()) {
            custo.setId(Long.parseLong(idText));
        }

        try {
            // Substitui vírgula por ponto para garantir o parse correto do BigDecimal
            custo.setImposto(parseBigDecimal(txtImposto.getText()));
            custo.setCustoVariavel(parseBigDecimal(txtCustoVariavel.getText()));
            custo.setCustoFixo(parseBigDecimal(txtCustoFixo.getText()));
            custo.setMargemLucro(parseBigDecimal(txtMargemLucro.getText()));

            if (custo.getCustoVariavel() == null || custo.getMargemLucro() == null) {
                throw new NumberFormatException("Custo Variável e Margem de Lucro não podem ser vazios.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valores numéricos inválidos. Use apenas números e ponto/vírgula decimal.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String dateText = txtDataProcessamento.getText().replaceAll("[^\\d/]", "");
            custo.setDataProcessamento(LocalDate.parse(dateText, DATE_FORMATTER));
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        custo.setTipoCusto((TipoCusto) cmbTipoCusto.getSelectedItem());

        try {
            custoService.salvarCusto(custo);
            JOptionPane.showMessageDialog(this, "Custo salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            carregarCustos();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar custo: " + e.getMessage(),
                    "Erro de Salvamento",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirCusto() {
        int selectedRow = tblCustos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um custo na tabela para excluir.", "Nenhuma Seleção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CustoModel custoSelecionado = custoTableModel.getCustoAt(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o registro de custo para '" + custoSelecionado.getTipoCusto().getDescricao() + "'?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                custoService.deletarCusto(custoSelecionado.getId());
                JOptionPane.showMessageDialog(this, "Custo excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                carregarCustos();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir custo: " + e.getMessage(),
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

            new CustoView().setVisible(true);
        });
    }

    /**
     * Converte uma String para BigDecimal, tratando campos vazios como nulos.
     * @param text O texto do campo.
     * @return Um BigDecimal ou null se o texto for vazio.
     * @throws NumberFormatException se o texto não for um número válido.
     */
    private BigDecimal parseBigDecimal(String text) throws NumberFormatException {
        String cleanedText = text.trim().replace(",", ".");
        return cleanedText.isEmpty() ? null : new BigDecimal(cleanedText);
    }
}