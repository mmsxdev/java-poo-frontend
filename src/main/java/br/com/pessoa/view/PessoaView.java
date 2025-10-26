package br.com.pessoa.view;

import br.com.pessoa.enums.TipoPessoa;
import br.com.pessoa.model.PessoaModel;
import br.com.pessoa.service.PessoaService;
import br.com.pessoa.table.PessoaTableModel;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class PessoaView extends JFrame {

    // --- Componentes da UI ---
    private JTextField txtId;
    private JTextField txtNomeCompleto;
    private JTextField txtCpfCnpj;
    private JTextField txtNumeroCtps;
    private JFormattedTextField txtDataNascimento;
    private JComboBox<TipoPessoa> cmbTipoPessoa;

    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnExcluir;
    private JButton btnLimpar;

    private JTable tblPessoas;

    // --- Lógica e Dados ---
    private final PessoaService pessoaService;
    private final PessoaTableModel pessoaTableModel;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PessoaView() {
        // --- Inicialização dos Serviços e Modelos ---
        this.pessoaService = new PessoaService();
        this.pessoaTableModel = new PessoaTableModel(new ArrayList<>());

        // --- Configuração da Janela Principal ---
        setTitle("Cadastro de Pessoas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela

        // --- Inicialização dos Componentes ---
        initComponents();
        initLayout();
        initActions();

        // --- Carregamento Inicial dos Dados ---
        carregarPessoas();
    }

    private void initComponents() {
        // --- Campos de Texto e ComboBox ---
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(230, 230, 230));

        txtNomeCompleto = new JTextField(30);
        txtCpfCnpj = new JTextField(15);
        txtNumeroCtps = new JTextField(10);

        try {
            MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
            dateFormatter.setPlaceholderCharacter('_');
            txtDataNascimento = new JFormattedTextField(dateFormatter);
        } catch (ParseException e) {
            // Fallback para um JTextField normal se a máscara falhar
            txtDataNascimento = new JFormattedTextField();
            e.printStackTrace();
        }

        cmbTipoPessoa = new JComboBox<>(TipoPessoa.values());
        cmbTipoPessoa.setRenderer(new TipoPessoaRenderer()); // <-- ADICIONE ESTA LINHA

        // --- Botões ---
        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");

        // --- Tabela ---
        tblPessoas = new JTable(pessoaTableModel);
        tblPessoas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void initLayout() {
        // --- Painel do Formulário (usando GridBagLayout para mais controle) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados da Pessoa"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaçamento
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.2; formPanel.add(txtId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(new JLabel("Nome Completo:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.8; gbc.gridwidth = 3; formPanel.add(txtNomeCompleto, gbc);
        gbc.gridwidth = 1; // Reset

        // Linha 1
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("CPF/CNPJ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.4; formPanel.add(txtCpfCnpj, gbc);

        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(new JLabel("Data Nasc.:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.3; formPanel.add(txtDataNascimento, gbc);

        // Linha 2
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Nº CTPS:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.4; formPanel.add(txtNumeroCtps, gbc);

        gbc.gridx = 2; gbc.gridy = 2; formPanel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 0.3; formPanel.add(cmbTipoPessoa, gbc);

        // --- Painel de Botões ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnNovo);
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnExcluir);
        buttonPanel.add(btnLimpar);

        // --- Painel Principal (Norte: formulário, Centro: tabela, Sul: botões) ---
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.add(formPanel, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(tblPessoas), BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initActions() {
        // --- Ações dos Botões ---
        btnNovo.addActionListener(e -> limparFormulario());
        btnLimpar.addActionListener(e -> limparFormulario());
        btnSalvar.addActionListener(e -> salvarPessoa());
        btnExcluir.addActionListener(e -> excluirPessoa());

        // --- Ação de Seleção na Tabela ---
        tblPessoas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblPessoas.getSelectedRow();
                if (selectedRow != -1) {
                    PessoaModel pessoaSelecionada = pessoaTableModel.getPessoaAt(selectedRow);
                    preencherFormulario(pessoaSelecionada);
                }
            }
        });
    }

    private void carregarPessoas() {
        try {
            pessoaTableModel.setPessoas(pessoaService.buscarTodasPessoas());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar pessoas do servidor: " + e.getMessage(),
                    "Erro de Conexão",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        txtId.setText("");
        txtNomeCompleto.setText("");
        txtCpfCnpj.setText("");
        txtNumeroCtps.setText("");
        txtDataNascimento.setValue(null); // Limpa o JFormattedTextField
        txtDataNascimento.setText("");
        cmbTipoPessoa.setSelectedIndex(0);
        tblPessoas.clearSelection();
        txtNomeCompleto.requestFocus();
    }

    private void preencherFormulario(PessoaModel pessoa) {
        txtId.setText(pessoa.getId() != null ? pessoa.getId().toString() : "");
        txtNomeCompleto.setText(pessoa.getNomeCompleto());
        txtCpfCnpj.setText(pessoa.getCpfCnpj());
        txtNumeroCtps.setText(pessoa.getNumeroCtps() != null ? pessoa.getNumeroCtps().toString() : "");
        txtDataNascimento.setText(pessoa.getDataNascimento() != null ? pessoa.getDataNascimento().format(DATE_FORMATTER) : "");
        cmbTipoPessoa.setSelectedItem(pessoa.getTipoPessoa());
    }

    private void salvarPessoa() {
        // --- Validações básicas ---
        if (txtNomeCompleto.getText().trim().isEmpty() || txtCpfCnpj.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e CPF/CNPJ são obrigatórios.", "Campos Inválidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- Coleta de dados do formulário ---
        PessoaModel pessoa = new PessoaModel();
        String idText = txtId.getText();
        if (idText != null && !idText.isEmpty()) {
            pessoa.setId(Long.parseLong(idText));
        }

        pessoa.setNomeCompleto(txtNomeCompleto.getText().trim());
        pessoa.setCpfCnpj(txtCpfCnpj.getText().trim());

        String ctpsText = txtNumeroCtps.getText().trim();
        if (!ctpsText.isEmpty()) {
            try {
                pessoa.setNumeroCtps(Long.parseLong(ctpsText));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Número da CTPS inválido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            // Remove caracteres não numéricos antes de fazer o parse
            String dateText = txtDataNascimento.getText().replaceAll("[^\\d/]", "");
            if (!dateText.replace("/", "").trim().isEmpty()) {
                pessoa.setDataNascimento(LocalDate.parse(dateText, DATE_FORMATTER));
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        pessoa.setTipoPessoa((TipoPessoa) cmbTipoPessoa.getSelectedItem());

        // --- Chamada ao serviço para salvar ---
        try {
            pessoaService.salvarPessoa(pessoa);
            JOptionPane.showMessageDialog(this, "Pessoa salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            carregarPessoas(); // Recarrega a tabela
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar pessoa: " + e.getMessage(),
                    "Erro de Salvamento",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirPessoa() {
        int selectedRow = tblPessoas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma pessoa na tabela para excluir.", "Nenhuma Seleção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PessoaModel pessoaSelecionada = pessoaTableModel.getPessoaAt(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir '" + pessoaSelecionada.getNomeCompleto() + "'?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                pessoaService.deletarPessoa(pessoaSelecionada.getId());
                JOptionPane.showMessageDialog(this, "Pessoa excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                carregarPessoas(); // Recarrega a tabela
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir pessoa: " + e.getMessage(),
                        "Erro de Exclusão",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Ponto de entrada para a aplicação.
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

            new PessoaView().setVisible(true);
        });
    }
}