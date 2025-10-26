package br.com.contato.view;

import br.com.contato.model.ContatoModel;
import br.com.contato.service.ContatoService;
import br.com.contato.table.ContatoTableModel;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;

public class ContatoView extends JFrame {

    // --- Componentes da UI ---
    private JTextField txtId;
    private JFormattedTextField txtTelefone;
    private JTextField txtEmail;
    private JTextField txtEndereco;

    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnExcluir;

    private JTable tblContatos;

    // --- Lógica e Dados ---
    private final ContatoService contatoService;
    private final ContatoTableModel contatoTableModel;

    public ContatoView() {
        this.contatoService = new ContatoService();
        this.contatoTableModel = new ContatoTableModel();

        setTitle("Cadastro de Contatos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        initLayout();
        initActions();

        carregarContatos();
    }

    private void initComponents() {
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(230, 230, 230));

        try {
            MaskFormatter phoneFormatter = new MaskFormatter("(##) #####-####");
            phoneFormatter.setPlaceholderCharacter('_');
            txtTelefone = new JFormattedTextField(phoneFormatter);
        } catch (ParseException e) {
            txtTelefone = new JFormattedTextField();
            e.printStackTrace();
        }

        txtEmail = new JTextField(30);
        txtEndereco = new JTextField(40);

        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnExcluir = new JButton("Excluir");

        tblContatos = new JTable(contatoTableModel);
        tblContatos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void initLayout() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Contato"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.1; formPanel.add(txtId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.9; formPanel.add(txtTelefone, gbc);

        // Linha 1
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.gridwidth = 3; formPanel.add(txtEmail, gbc);
        gbc.gridwidth = 1;

        // Linha 2
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; gbc.gridwidth = 3; formPanel.add(txtEndereco, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnNovo);
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnExcluir);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.add(formPanel, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(tblContatos), BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initActions() {
        btnNovo.addActionListener(e -> limparFormulario());
        btnSalvar.addActionListener(e -> salvarContato());
        btnExcluir.addActionListener(e -> excluirContato());

        tblContatos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblContatos.getSelectedRow();
                if (selectedRow != -1) {
                    ContatoModel contatoSelecionado = contatoTableModel.getContatoAt(selectedRow);
                    preencherFormulario(contatoSelecionado);
                }
            }
        });
    }

    private void carregarContatos() {
        try {
            contatoTableModel.setContatos(contatoService.buscarTodosContatos());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar contatos do servidor: " + e.getMessage(),
                    "Erro de Conexão",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        txtId.setText("");
        txtTelefone.setValue(null);
        txtTelefone.setText("");
        txtEmail.setText("");
        txtEndereco.setText("");
        tblContatos.clearSelection();
        txtTelefone.requestFocus();
    }

    private void preencherFormulario(ContatoModel contato) {
        txtId.setText(contato.getId() != null ? contato.getId().toString() : "");
        txtTelefone.setText(contato.getTelefone());
        txtEmail.setText(contato.getEmail());
        txtEndereco.setText(contato.getEndereco());
    }

    private void salvarContato() {
        if (txtEmail.getText().trim().isEmpty() || txtTelefone.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Telefone e E-mail são obrigatórios.", "Campos Inválidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ContatoModel contato = new ContatoModel();
        String idText = txtId.getText();
        if (idText != null && !idText.isEmpty()) {
            contato.setId(Long.parseLong(idText));
        }

        // Pega o valor do campo formatado, removendo a máscara
        String telefone = txtTelefone.getText().replaceAll("[^0-9]", "");
        contato.setTelefone(telefone);
        contato.setEmail(txtEmail.getText().trim());
        contato.setEndereco(txtEndereco.getText().trim());

        try {
            contatoService.salvarContato(contato);
            JOptionPane.showMessageDialog(this, "Contato salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            carregarContatos();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar contato: " + e.getMessage(),
                    "Erro de Salvamento",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirContato() {
        int selectedRow = tblContatos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um contato na tabela para excluir.", "Nenhuma Seleção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ContatoModel contatoSelecionado = contatoTableModel.getContatoAt(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o contato de '" + contatoSelecionado.getEmail() + "'?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                contatoService.deletarContato(contatoSelecionado.getId());
                JOptionPane.showMessageDialog(this, "Contato excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                carregarContatos();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir contato: " + e.getMessage(),
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

            new ContatoView().setVisible(true);
        });
    }
}