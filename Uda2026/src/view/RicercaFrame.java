package view;

import controller.Gestore;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;

/**
 * RicercaFrame - finestra separata per la ricerca avanzata dei veicoli.
 * Non contiene logica: chiama solo g.cerca() e mostra i risultati.
 *
 * @author Fornacciari Samuele, Berni Alessio
 */
public class RicercaFrame extends javax.swing.JFrame {

    private final Gestore g; // stesso Gestore passato dal MainFrame

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
     * @param g gestore condiviso con il MainFrame
     */
    public RicercaFrame(Gestore g) {
        this.g = g;
        initComponents();
        // mostra subito tutti i veicoli
        aggiornaTabella(g.getVeicoli());
    }

    /**
     * Costruisce tutti i componenti grafici della finestra.
     */
    private void initComponents() {
        setTitle("Ricerca Veicoli");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(860, 420);
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

        JButton btnChiudi = new JButton("Chiudi");
        btnChiudi.addActionListener(e -> dispose());
        pannelloRicerca.add(btnChiudi);

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

        // etichetta contatore risultati
        JLabel lblContatore = new JLabel(" ");
        pannelloPrincipale.add(lblContatore, BorderLayout.SOUTH);

        add(pannelloPrincipale);
    }

    /**
     * Passa targa e tipo al Gestore e aggiorna la tabella con i risultati.
     * Tutta la logica di filtro e' nel Gestore.
     */
    private void eseguiRicerca() {
        String targa = campTarga.getText();
        String tipo  = (String) comboTipo.getSelectedItem();
        ArrayList<Veicolo> risultati = g.cerca(targa, tipo); // logica nel Gestore
        aggiornaTabella(risultati);
    }

    /**
     * Riempie la tabella con la lista passata.
     * Questo e' l'unico compito della view: mostrare i dati.
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
