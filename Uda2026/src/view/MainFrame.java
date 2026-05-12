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
 * Mostra la lista dei veicoli in una JTable con ricerca per targa e tipo.
 *
 * @author Fornacciari Samuele, Berni Alessio
 */
public class MainFrame extends javax.swing.JFrame {

    protected Gestore g = new Gestore();

    // componenti principali
    private JTable tabellaVeicoli;
    private DefaultTableModel modelloTabella;
    private JTextField campoCerca;
    private JComboBox<String> comboTipo;

    // colonne della tabella (campi comuni a tutti i veicoli)
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
     * Inizializza tutti i componenti della finestra.
     */
    private void initComponents() {
        setTitle("Gestione Flotta Veicoli Aziendali");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1000, 520);
        setLocationRelativeTo(null);

        // chiusura con conferma
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                g.esci();
            }
        });

        // ---- menu bar ----
        JMenuBar menuBar = new JMenuBar();

        // menu File
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

        // menu Modifica
        JMenu menuModifica = new JMenu("Modifica");

        JMenuItem itemInserisci = new JMenuItem("Inserisci");
        itemInserisci.addActionListener(e -> apriInserisci());

        JMenuItem itemElimina = new JMenuItem("Elimina");
        itemElimina.addActionListener(e -> eliminaSelezionato());

        JMenuItem itemModifica = new JMenuItem("Modifica");
        itemModifica.addActionListener(e -> apriModifica());

        JMenuItem itemLista = new JMenuItem("Visualizza Lista");
        itemLista.addActionListener(e -> aggiornaTabella(g.getVeicoli()));

        menuModifica.add(itemInserisci);
        menuModifica.add(itemElimina);
        menuModifica.add(itemModifica);
        menuModifica.add(itemLista);

        // menu Info
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

        // premendo Invio nel campo testo lancia la ricerca
        campoCerca.addActionListener(e -> eseguiRicerca());

        pannelloPrincipale.add(pannelloRicerca, BorderLayout.NORTH);

        // ---- tabella ----
        modelloTabella = new DefaultTableModel(COLONNE, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // non si modifica direttamente nella tabella
            }
        };

        tabellaVeicoli = new JTable(modelloTabella);
        tabellaVeicoli.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabellaVeicoli.getTableHeader().setReorderingAllowed(false);

        // doppio click -> apre finestra di modifica
        tabellaVeicoli.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    apriModifica();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabellaVeicoli);
        pannelloPrincipale.add(scrollPane, BorderLayout.CENTER);

        // ---- pulsanti in basso ----
        JPanel pannelloPulsanti = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnInserisci = new JButton("Inserisci");
        btnInserisci.addActionListener(e -> apriInserisci());

        JButton btnModifica = new JButton("Modifica");
        btnModifica.addActionListener(e -> apriModifica());

        JButton btnElimina = new JButton("Elimina");
        btnElimina.addActionListener(e -> eliminaSelezionato());

        pannelloPulsanti.add(btnInserisci);
        pannelloPulsanti.add(btnModifica);
        pannelloPulsanti.add(btnElimina);

        pannelloPrincipale.add(pannelloPulsanti, BorderLayout.SOUTH);

        add(pannelloPrincipale);
        pack();
        setSize(1000, 520); // forza dimensione dopo pack
    }

    // ---- OPERAZIONI ----

    /**
     * Apre la finestra di inserimento nuovo veicolo.
     */
    private void apriInserisci() {
        ModificaCatalogoFrame f = new ModificaCatalogoFrame(this, g, null);
        f.setVisible(true);
        aggiornaTabella(g.getVeicoli());
    }

    /**
     * Apre la finestra di modifica per il veicolo selezionato nella tabella.
     */
    private void apriModifica() {
        int riga = tabellaVeicoli.getSelectedRow();
        if (riga == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleziona un veicolo dalla lista.",
                "Nessuna selezione", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String targa = (String) modelloTabella.getValueAt(riga, 0);
        // cerca il veicolo nel gestore tramite targa
        Veicolo trovato = null;
        for (Veicolo v : g.getVeicoli()) {
            if (v.getTarga().equals(targa)) {
                trovato = v;
                break;
            }
        }
        ModificaCatalogoFrame f = new ModificaCatalogoFrame(this, g, trovato);
        f.setVisible(true);
        aggiornaTabella(g.getVeicoli());
    }

    /**
     * Elimina il veicolo selezionato dopo conferma.
     */
    private void eliminaSelezionato() {
        int riga = tabellaVeicoli.getSelectedRow();
        if (riga == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleziona un veicolo dalla lista.",
                "Nessuna selezione", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int risposta = JOptionPane.showConfirmDialog(this,
            "Sei sicuro di voler eliminare il veicolo selezionato?\nL'operazione non può essere annullata.",
            "Conferma eliminazione",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (risposta == JOptionPane.YES_OPTION) {
            String targa = (String) modelloTabella.getValueAt(riga, 0);
            for (Veicolo v : g.getVeicoli()) {
                if (v.getTarga().equals(targa)) {
                    g.removeVeicolo(v);
                    break;
                }
            }
            aggiornaTabella(g.getVeicoli());
        }
    }

    /**
     * Filtra la lista per targa (parziale) e/o tipo selezionato.
     */
    private void eseguiRicerca() {
        String testo = campoCerca.getText().trim().toLowerCase();
        String tipoSelezionato = (String) comboTipo.getSelectedItem();

        ArrayList<Veicolo> risultati = new ArrayList<>();
        for (Veicolo v : g.getVeicoli()) {
            boolean targaOk = testo.isEmpty() || v.getTarga().toLowerCase().contains(testo);
            boolean tipoOk = tipoSelezionato.equals("Tutti")
                || (tipoSelezionato.equals("Automobile") && v instanceof Auto)
                || (tipoSelezionato.equals("Furgone") && v instanceof Furgone);

            if (targaOk && tipoOk) {
                risultati.add(v);
            }
        }
        aggiornaTabella(risultati);
    }

    /**
     * Aggiorna la JTable con la lista di veicoli passata.
     *
     * @param lista lista di veicoli da visualizzare
     */
    public void aggiornaTabella(ArrayList<Veicolo> lista) {
        modelloTabella.setRowCount(0);
        for (Veicolo v : lista) {
            String tipo = (v instanceof Auto) ? "Automobile" : "Furgone";
            modelloTabella.addRow(new Object[]{
                v.getTarga(),
                tipo,
                v.getMarca(),
                v.getModello(),
                v.getAnno(),
                v.getKm(),
                v.getScadenzaAssicurazione(),
                v.getScadenzaRevisione(),
                v.getScadenzaTagliando(),
                v.getClasseEnergetica(),
                v.getConsumo()
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
            // se Nimbus non c'è si usa il look and feel di default
        }

        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
