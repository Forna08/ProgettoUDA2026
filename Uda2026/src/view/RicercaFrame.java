package view;

import controller.Gestore;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;

/**
 * RicercaFrame - finestra separata per la ricerca avanzata dei veicoli.
 * Permette di filtrare per targa (parziale) e tipo (Auto / Furgone).
 *
 * @author Fornacciari Samuele, Berni Alessio
 */
public class RicercaFrame extends javax.swing.JFrame {

    private final Gestore g;

    private JTextField campTarga;
    private JComboBox<String> comboTipo;
    private JTable tabellaRisultati;
    private DefaultTableModel modelloTabella;

    private static final String[] COLONNE = {
        "Targa", "Tipo", "Marca", "Modello", "Anno", "Km",
        "Scad. Assicurazione", "Scad. Revisione", "Scad. Tagliando"
    };

    /**
     * Costruttore.
     *
     * @param g gestore del model per accedere alla lista veicoli
     */
    public RicercaFrame(Gestore g) {
        this.g = g;
        initComponents();
    }

    /**
     * Inizializza tutti i componenti della finestra.
     */
    private void initComponents() {
        setTitle("Ricerca Veicoli");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 420);
        setLocationRelativeTo(null);

        JPanel pannelloPrincipale = new JPanel(new BorderLayout(5, 5));
        pannelloPrincipale.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- barra di ricerca ----
        JPanel pannelloRicerca = new JPanel(new FlowLayout(FlowLayout.LEFT));

        pannelloRicerca.add(new JLabel("Targa:"));
        campTarga = new JTextField(12);
        pannelloRicerca.add(campTarga);

        pannelloRicerca.add(new JLabel("  Tipo:"));
        comboTipo = new JComboBox<>(new String[]{"Tutti", "Automobile", "Furgone"});
        pannelloRicerca.add(comboTipo);

        JButton btnCerca = new JButton("Cerca");
        btnCerca.addActionListener(e -> eseguiRicerca());
        pannelloRicerca.add(btnCerca);

        // premendo Invio nel campo targa lancia la ricerca
        campTarga.addActionListener(e -> eseguiRicerca());

        JButton btnHome = new JButton("Chiudi");
        btnHome.addActionListener(e -> dispose());
        pannelloRicerca.add(btnHome);

        pannelloPrincipale.add(pannelloRicerca, BorderLayout.NORTH);

        // ---- tabella risultati ----
        modelloTabella = new DefaultTableModel(COLONNE, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tabellaRisultati = new JTable(modelloTabella);
        tabellaRisultati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabellaRisultati.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tabellaRisultati);
        pannelloPrincipale.add(scrollPane, BorderLayout.CENTER);

        add(pannelloPrincipale);

        // mostra subito tutti i veicoli
        aggiornaTabella(g.getVeicoli());
    }

    /**
     * Filtra i veicoli per targa (parziale, case-insensitive) e tipo.
     */
    private void eseguiRicerca() {
        String testo = campTarga.getText().trim().toLowerCase();
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
     * Riempie la tabella con la lista passata.
     *
     * @param lista lista di veicoli da visualizzare
     */
    private void aggiornaTabella(ArrayList<Veicolo> lista) {
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
                v.getScadenzaTagliando()
            });
        }
    }
}
