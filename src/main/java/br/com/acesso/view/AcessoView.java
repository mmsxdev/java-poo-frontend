package br.com.acesso.view;

import br.com.acesso.enums.TipoAcesso;
import br.com.acesso.model.AcessoModel;
import br.com.acesso.service.AcessoService;
import br.com.acesso.table.AcessoTableModel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AcessoView extends JFrame {

    // --- Componentes da UI ---
    private JTextField txtId;
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JComboBox<TipoAcesso> cmbTipoAcesso;

    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnExcluir;

    private JTable tblAcessos;

    // --- Lógica e Dados ---
    private final AcessoService acessoService;
    private final AcessoTableModel acessoTableModel;

    public AcessoView() {
        this.acessoService = new AcessoService();
        this.acessoTableModel = new AcessoTableModel();

        setTitle("Cadastro de Acessos de Usuário");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        initLayout();
        initActions();

        carregarAcessos();
    }

    private void initComponents() {
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(230, 230, 230));

        txtUsuario = new JTextField(20);
        txtSenha = new JPasswordField(20);

        cmbTipoAcesso = new JComboBox<>(TipoAcesso.values());
        cmbTipoAcesso.setRenderer(new TipoAcessoRenderer());

        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnExcluir = new JButton("Excluir");

        tblAcessos = new JTable(acessoTableModel);
        tblAcessos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void initLayout() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Acesso"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.2; formPanel.add(txtId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(new JLabel("Usuário:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.8; formPanel.add(txtUsuario, gbc);

        // Linha 1
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.2; formPanel.add(txtSenha, gbc);

        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(new JLabel("Tipo de Acesso:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.8; formPanel.add(cmbTipoAcesso, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnNovo);
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnExcluir);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.add(formPanel, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(tblAcessos), BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initActions() {
        btnNovo.addActionListener(e -> limparFormulario());
        btnSalvar.addActionListener(e -> salvarAcesso());
        btnExcluir.addActionListener(e -> excluirAcesso());

        tblAcessos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblAcessos.getSelectedRow();
                if (selectedRow != -1) {
                    AcessoModel acessoSelecionado = acessoTableModel.getAcessoAt(selectedRow);
                    preencherFormulario(acessoSelecionado);
                }
            }
        });
    }

    private void carregarAcessos() {
        try {
            acessoTableModel.setAcessos(acessoService.buscarTodosAcessos());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar acessos do servidor: " + e.getMessage(),
                    "Erro de Conexão",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        txtId.setText("");
        txtUsuario.setText("");
        txtSenha.setText("");
        cmbTipoAcesso.setSelectedIndex(0);
        tblAcessos.clearSelection();
        txtUsuario.requestFocus();
    }

    private void preencherFormulario(AcessoModel acesso) {
        txtId.setText(acesso.getId() != null ? acesso.getId().toString() : "");
        txtUsuario.setText(acesso.getUsuario());
        cmbTipoAcesso.setSelectedItem(acesso.getTipoAcesso());
        // A senha nunca é preenchida por segurança. O campo fica pronto para uma nova senha.
        txtSenha.setText("");
        txtSenha.requestFocus();
    }

    private void salvarAcesso() {
        String senha = new String(txtSenha.getPassword());

        // Validação: ao criar um novo usuário, a senha é obrigatória.
        if (txtUsuario.getText().trim().isEmpty() || (txtId.getText().isEmpty() && senha.isEmpty())) {
            JOptionPane.showMessageDialog(this, "Usuário e Senha são obrigatórios para novos registros.", "Campos Inválidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AcessoModel acesso = new AcessoModel();
        String idText = txtId.getText();
        if (idText != null && !idText.isEmpty()) {
            acesso.setId(Long.parseLong(idText));
        }

        acesso.setUsuario(txtUsuario.getText().trim());
        // A senha só é enviada se o usuário digitou algo.
        if (!senha.isEmpty()) {
            acesso.setSenha(senha);
        }
        acesso.setTipoAcesso((TipoAcesso) cmbTipoAcesso.getSelectedItem());

        try {
            acessoService.salvarAcesso(acesso);
            JOptionPane.showMessageDialog(this, "Acesso salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            carregarAcessos();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar acesso: " + e.getMessage(),
                    "Erro de Salvamento",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirAcesso() {
        int selectedRow = tblAcessos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um acesso na tabela para excluir.", "Nenhuma Seleção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AcessoModel acessoSelecionado = acessoTableModel.getAcessoAt(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o usuário '" + acessoSelecionado.getUsuario() + "'?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                acessoService.deletarAcesso(acessoSelecionado.getId());
                JOptionPane.showMessageDialog(this, "Acesso excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                carregarAcessos();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir acesso: " + e.getMessage(),
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

            new AcessoView().setVisible(true);
        });
    }
}