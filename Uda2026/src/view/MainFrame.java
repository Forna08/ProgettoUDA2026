package view;

import controller.Gestore;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;

/**
 * MainFrame - finestra principale dell'applicazione.
 * Contiene solo logica grafica (for, if di base).
 * Tutta la logica di business e' delegata al Gestore.
 *
 * @author Fornacciari Samuele, Berni Alessio
 */
public class MainFrame extends javax.swing.JFrame {

    // unico Gestore condiviso con tutte le finestre figlie
    private final Gestore g = new Gestore();

    private JTable tabellaVeicoli;
    private DefaultTableModel modelloTabella;
    private JTextField campoCerca;
    private JComboBox<String> comboTipo;

    private static final String[] COLONNE = {
        "Targa", "Tipo", "Marca", "Modello", "Anno", "Km",
        "Scad. Assicurazione", "Scad. Revisione", "Scad. Tagliando",
        "Classe Energ.", "Consumo"
    };

    /**
     * Costruttore: inizializza la finestra principale.
     */
    public MainFrame() {
        initComponents();
    }

    /**
     * Costruisce tutti i componenti grafici della finestra.
     */
    private void initComponents() {
        setTitle("Gestione Flotta Veicoli Aziendali");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1000, 520);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                g.esci();
            }
        });

        // ---- menu bar ----
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = new JMenu("File");
        JMenuItem itemApri = new JMenuItem("Apri");
        itemApri.addActionListener(e -> {
            g.apri();
            aggiornaTabella(g.getVeicoli());
        });
        JMenuItem itemSalva = new JMenuItem("Salva");
        itemSalva.addActionListener(e -> g.salva());
        JMenuItem itemSalvaConNome = new JMenuItem("Salva con Nome");
        itemSalvaConNome.addActionListener(e -> g.salvaConNome());
        JMenuItem itemEsci = new JMenuItem("Esci");
        itemEsci.addActionListener(e -> g.esci());
        menuFile.add(itemApri);
        menuFile.add(itemSalva);
        menuFile.add(itemSalvaConNome);
        menuFile.addSeparator();
        menuFile.add(itemEsci);

        JMenu menuModifica = new JMenu("Modifica");
        JMenuItem itemInserisci = new JMenuItem("Inserisci");
        itemInserisci.addActionListener(e -> apriInserisci());
        JMenuItem itemElimina = new JMenuItem("Elimina");
        itemElimina.addActionListener(e -> apriElimina());
        JMenuItem itemModifica = new JMenuItem("Modifica");
        itemModifica.addActionListener(e -> apriModifica());
        JMenuItem itemLista = new JMenuItem("Visualizza Lista");
        itemLista.addActionListener(e -> g.visualizzaLista());
        menuModifica.add(itemInserisci);
        menuModifica.add(itemElimina);
        menuModifica.add(itemModifica);
        menuModifica.add(itemLista);

        JMenu menuInfo = new JMenu("Info");
        JMenuItem itemAbout = new JMenuItem("About");
        itemAbout.addActionListener(e -> g.about());
        JMenuItem itemCredits = new JMenuItem("Credits");
        itemCredits.addActionListener(e -> g.credits());
        menuInfo.add(itemAbout);
        menuInfo.add(itemCredits);

        menuBar.add(menuFile);
        menuBar.add(menuModifica);
        menuBar.add(menuInfo);
        setJMenuBar(menuBar);

        // ---- pannello principale ----
        JPanel pannelloPrincipale = new JPanel(new BorderLayout(5, 5));
        pannelloPrincipale.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- barra di ricerca ----
        JPanel pannelloRicerca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pannelloRicerca.add(new JLabel("Cerca per targa:"));
        campoCerca = new JTextField(15);
        pannelloRicerca.add(campoCerca);
        pannelloRicerca.add(new JLabel("  Tipo:"));
        comboTipo = new JComboBox<>(new String[]{"Tutti", "Automobile", "Furgone"});
        pannelloRicerca.add(comboTipo);
        JButton btnCerca = new JButton("Cerca");
        btnCerca.addActionListener(e -> eseguiRicerca());
        pannelloRicerca.add(btnCerca);
        campoCerca.addActionListener(e -> eseguiRicerca());
        pannelloPrincipale.add(pannelloRicerca, BorderLayout.NORTH);

        // ---- tabella ----
        modelloTabella = new DefaultTableModel(COLONNE, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tabellaVeicoli = new JTable(modelloTabella);
        tabellaVeicoli.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabellaVeicoli.getTableHeader().setReorderingAllowed(false);
        tabellaVeicoli.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    apriModifica();
                }
            }
        });
        pannelloPrincipale.add(new JScrollPane(tabellaVeicoli), BorderLayout.CENTER);

        // ---- pulsanti in basso ----
        JPanel pannelloPulsanti = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnInserisci = new JButton("Inserisci");
        btnInserisci.addActionListener(e -> apriInserisci());
        JButton btnModifica = new JButton("Modifica");
        btnModifica.addActionListener(e -> apriModifica());
        JButton btnElimina = new JButton("Elimina");
        btnElimina.addActionListener(e -> apriElimina());
        pannelloPulsanti.add(btnInserisci);
        pannelloPulsanti.add(btnModifica);
        pannelloPulsanti.add(btnElimina);
        pannelloPrincipale.add(pannelloPulsanti, BorderLayout.SOUTH);

        add(pannelloPrincipale);
        setSize(1000, 520);
    }

    /**
     * Apre il dialogo di inserimento e aggiorna la tabella dopo la chiusura.
     */
    private void apriInserisci() {
        ModificaCatalogoFrame f = new ModificaCatalogoFrame(this, g, null);
        f.setVisible(true);
        aggiornaTabella(g.getVeicoli());
    }

    /**
     * Legge la targa dalla riga selezionata e apre il dialogo di modifica.
     */
    private void apriModifica() {
        int riga = tabellaVeicoli.getSelectedRow();
        if (riga == -1) {
            g.NessunaSelezione(); // messaggio nel Gestore
            return;
        }
        String targa = (String) modelloTabella.getValueAt(riga, 0);
        Veicolo trovato = g.getPerTarga(targa);
        ModificaCatalogoFrame f = new ModificaCatalogoFrame(this, g, trovato);
        f.setVisible(true);
        aggiornaTabella(g.getVeicoli());
    }

    /**
     * Legge la targa dalla riga selezionata, chiede conferma al Gestore
     * e poi chiama eliminaPerTarga().
     */
    private void apriElimina() {
        int riga = tabellaVeicoli.getSelectedRow();
        if (riga == -1) {
            g.NessunaSelezione(); // messaggio nel Gestore
            return;
        }
        if (g.confermaEliminazione()) { // conferma nel Gestore
            String targa = (String) modelloTabella.getValueAt(riga, 0);
            g.eliminaPerTarga(targa);
            aggiornaTabella(g.getVeicoli());
        }
    }

    /**
     * Passa targa e tipo al Gestore e aggiorna la tabella con i risultati.
     */
    private void eseguiRicerca() {
        String targa = campoCerca.getText();
        String tipo = (String) comboTipo.getSelectedItem();
        aggiornaTabella(g.cerca(targa, tipo));
    }

    /**
     * Aggiorna la JTable con la lista passata.
     *
     * @param lista lista di veicoli da visualizzare
     */
    public void aggiornaTabella(ArrayList<Veicolo> lista) {
        modelloTabella.setRowCount(0);
        for (Veicolo v : lista) {
            String tipo = (v instanceof Auto) ? "Automobile" : "Furgone";
            modelloTabella.addRow(new Object[]{
                v.getTarga(), tipo, v.getMarca(), v.getModello(),
                v.getAnno(), v.getKm(),
                v.getScadenzaAssicurazione(), v.getScadenzaRevisione(),
                v.getScadenzaTagliando(), v.getClasseEnergetica(), v.getConsumo()
            });
        }
    }

    /**
     * Punto di ingresso dell'applicazione.
     *
     * @param args argomenti da riga di comando (non usati)
     */
    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            // look and feel di default
        }
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
