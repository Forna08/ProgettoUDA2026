package view;

import controller.Gestore;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import model.*;

/**
 * ModificaCatalogoFrame - finestra modale per inserire o modificare un veicolo.
 * Se viene passato un veicolo esistente i campi vengono pre-popolati (modalità modifica).
 * Se viene passato null si tratta di un inserimento nuovo.
 *
 * @author Fornacciari Samuele, Berni Alessio
 */
public class ModificaCatalogoFrame extends JDialog {

    private final Gestore g;
    private final Veicolo veicoloDaModificare; // null se si sta inserendo

    // campi comuni
    private JTextField campTarga;
    private JComboBox<String> comboTipo;
    private JTextField campMarca;
    private JTextField campModello;
    private JTextField campAnno;
    private JTextField campKm;
    private JTextField campAssicurazione;
    private JTextField campRevisione;
    private JTextField campTagliando;
    private JTextField campClasseEnergetica;
    private JTextField campConsumo;

    // campi solo Auto
    private JPanel pannelloAuto;
    private JTextField campPosti;
    private JTextField campTipoAuto;
    private JTextField campVMax;
    private JTextField campColore;

    // campi solo Furgone
    private JPanel pannelloFurgone;
    private JTextField campVolume;
    private JTextField campAutonomia;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Costruttore.
     *
     * @param parent finestra padre
     * @param g gestore del model
     * @param v veicolo da modificare, oppure null per inserimento
     */
    public ModificaCatalogoFrame(Frame parent, Gestore g, Veicolo v) {
        super(parent, v == null ? "Inserisci Veicolo" : "Modifica Veicolo", true);
        this.g = g;
        this.veicoloDaModificare = v;
        initComponents();
        if (v != null) {
            prePopola(v);
        }
    }

    /**
     * Costruisce tutti i componenti della finestra.
     */
    private void initComponents() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(420, 560);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel pannelloPrincipale = new JPanel();
        pannelloPrincipale.setLayout(new BoxLayout(pannelloPrincipale, BoxLayout.Y_AXIS));
        pannelloPrincipale.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // ---- campi comuni ----
        campTarga = new JTextField(10);
        comboTipo = new JComboBox<>(new String[]{"Automobile", "Furgone"});
        campMarca = new JTextField(10);
        campModello = new JTextField(10);
        campAnno = new JTextField(10);
        campKm = new JTextField(10);
        campAssicurazione = new JTextField(10);
        campRevisione = new JTextField(10);
        campTagliando = new JTextField(10);
        campClasseEnergetica = new JTextField(10);
        campConsumo = new JTextField(10);

        pannelloPrincipale.add(creaRiga("Targa (7 car.):", campTarga));
        pannelloPrincipale.add(creaRiga("Tipo:", comboTipo));
        pannelloPrincipale.add(creaRiga("Marca:", campMarca));
        pannelloPrincipale.add(creaRiga("Modello:", campModello));
        pannelloPrincipale.add(creaRiga("Anno:", campAnno));
        pannelloPrincipale.add(creaRiga("Km:", campKm));
        pannelloPrincipale.add(creaRiga("Scad. Assicurazione (yyyy-MM-dd):", campAssicurazione));
        pannelloPrincipale.add(creaRiga("Scad. Revisione (yyyy-MM-dd):", campRevisione));
        pannelloPrincipale.add(creaRiga("Scad. Tagliando (yyyy-MM-dd):", campTagliando));
        pannelloPrincipale.add(creaRiga("Classe Energetica:", campClasseEnergetica));
        pannelloPrincipale.add(creaRiga("Consumo (l/100km):", campConsumo));

        // ---- campi auto ----
        campPosti = new JTextField(10);
        campTipoAuto = new JTextField(10);
        campVMax = new JTextField(10);
        campColore = new JTextField(10);

        pannelloAuto = new JPanel();
        pannelloAuto.setLayout(new BoxLayout(pannelloAuto, BoxLayout.Y_AXIS));
        pannelloAuto.setBorder(BorderFactory.createTitledBorder("Dati Auto"));
        pannelloAuto.add(creaRiga("Posti:", campPosti));
        pannelloAuto.add(creaRiga("Tipo auto (es. berlina):", campTipoAuto));
        pannelloAuto.add(creaRiga("Vel. Max (km/h):", campVMax));
        pannelloAuto.add(creaRiga("Colore:", campColore));

        // ---- campi furgone ----
        campVolume = new JTextField(10);
        campAutonomia = new JTextField(10);

        pannelloFurgone = new JPanel();
        pannelloFurgone.setLayout(new BoxLayout(pannelloFurgone, BoxLayout.Y_AXIS));
        pannelloFurgone.setBorder(BorderFactory.createTitledBorder("Dati Furgone"));
        pannelloFurgone.add(creaRiga("Volume (m³):", campVolume));
        pannelloFurgone.add(creaRiga("Autonomia (km):", campAutonomia));

        pannelloPrincipale.add(pannelloAuto);
        pannelloPrincipale.add(pannelloFurgone);

        // mostra/nasconde il pannello giusto quando si cambia tipo
        comboTipo.addActionListener(e -> aggiornaPannelloTipo());
        aggiornaPannelloTipo(); // impostazione iniziale

        // ---- pulsanti ----
        JPanel pannelloPulsanti = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnConferma = new JButton("Conferma");
        btnConferma.addActionListener(e -> conferma());

        JButton btnAnnulla = new JButton("Annulla");
        btnAnnulla.addActionListener(e -> dispose());

        pannelloPulsanti.add(btnConferma);
        pannelloPulsanti.add(btnAnnulla);

        add(new JScrollPane(pannelloPrincipale), BorderLayout.CENTER);
        add(pannelloPulsanti, BorderLayout.SOUTH);
    }

    /**
     * Crea una riga etichetta + campo da aggiungere al pannello.
     */
    private JPanel creaRiga(String etichetta, JComponent campo) {
        JPanel riga = new JPanel(new BorderLayout(5, 2));
        riga.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        riga.add(new JLabel(etichetta), BorderLayout.WEST);
        riga.add(campo, BorderLayout.EAST);
        riga.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        return riga;
    }

    /**
     * Mostra il pannello del tipo selezionato e nasconde l'altro.
     */
    private void aggiornaPannelloTipo() {
        boolean isAuto = comboTipo.getSelectedItem().equals("Automobile");
        pannelloAuto.setVisible(isAuto);
        pannelloFurgone.setVisible(!isAuto);
        revalidate();
        repaint();
    }

    /**
     * Pre-popola i campi con i dati del veicolo da modificare.
     *
     * @param v veicolo da cui leggere i dati
     */
    private void prePopola(Veicolo v) {
        campTarga.setText(v.getTarga());
        campTarga.setEditable(false); // la targa è la chiave, non si cambia
        campMarca.setText(v.getMarca());
        campModello.setText(v.getModello());
        campAnno.setText(String.valueOf(v.getAnno()));
        campKm.setText(String.valueOf(v.getKm()));
        campAssicurazione.setText(v.getScadenzaAssicurazione().format(FMT));
        campRevisione.setText(v.getScadenzaRevisione().format(FMT));
        campTagliando.setText(v.getScadenzaTagliando().format(FMT));
        campClasseEnergetica.setText(v.getClasseEnergetica());
        campConsumo.setText(String.valueOf(v.getConsumo()));

        if (v instanceof Auto) {
            comboTipo.setSelectedItem("Automobile");
            Auto a = (Auto) v;
            campPosti.setText(String.valueOf(a.getPosti()));
            campTipoAuto.setText(a.getTipo());
            campVMax.setText(String.valueOf(a.getvMax()));
            campColore.setText(a.getColore());
        } else {
            comboTipo.setSelectedItem("Furgone");
            Furgone f = (Furgone) v;
            campVolume.setText(String.valueOf(f.getVolume()));
            campAutonomia.setText(String.valueOf(f.getAutonomia()));
        }
        comboTipo.setEnabled(false); // non si cambia tipo in modifica
        aggiornaPannelloTipo();
    }

    /**
     * Legge i campi, valida e salva il veicolo nel gestore.
     */
    private void conferma() {
        // validazione campi obbligatori comuni
        String targa = campTarga.getText().trim();
        String marca = campMarca.getText().trim();
        String modello = campModello.getText().trim();
        String annoStr = campAnno.getText().trim();
        String kmStr = campKm.getText().trim();
        String assicStr = campAssicurazione.getText().trim();
        String revStr = campRevisione.getText().trim();
        String taglStr = campTagliando.getText().trim();
        String classeEn = campClasseEnergetica.getText().trim();
        String consumoStr = campConsumo.getText().trim();

        if (targa.isEmpty() || marca.isEmpty() || modello.isEmpty()
                || annoStr.isEmpty() || kmStr.isEmpty() || assicStr.isEmpty()
                || revStr.isEmpty() || taglStr.isEmpty() || classeEn.isEmpty()
                || consumoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Tutti i campi sono obbligatori.",
                "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // parsing valori numerici e date
        int anno, km;
        double consumo;
        LocalDate assic, rev, tagl;

        try {
            anno = Integer.parseInt(annoStr);
            km = Integer.parseInt(kmStr);
            consumo = Double.parseDouble(consumoStr);
            assic = LocalDate.parse(assicStr, FMT);
            rev = LocalDate.parse(revStr, FMT);
            tagl = LocalDate.parse(taglStr, FMT);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Anno, Km e Consumo devono essere numeri validi.",
                "Errore formato", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                "Le date devono essere nel formato yyyy-MM-dd (es. 2024-12-31).",
                "Errore formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean isAuto = comboTipo.getSelectedItem().equals("Automobile");

        try {
            if (isAuto) {
                // campi extra auto
                String postiStr = campPosti.getText().trim();
                String tipoAuto = campTipoAuto.getText().trim();
                String vMaxStr = campVMax.getText().trim();
                String colore = campColore.getText().trim();

                if (postiStr.isEmpty() || tipoAuto.isEmpty() || vMaxStr.isEmpty() || colore.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Compila tutti i campi specifici dell'automobile.",
                        "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int posti = Integer.parseInt(postiStr);
                int vMax = Integer.parseInt(vMaxStr);

                Auto nuovaAuto = new Auto(targa, marca, modello, anno, km,
                        assic, rev, tagl, classeEn, consumo, posti, tipoAuto, vMax, colore);

                if (veicoloDaModificare != null) {
                    g.removeVeicolo(veicoloDaModificare);
                }
                g.addVeicolo(nuovaAuto);

            } else {
                // campi extra furgone
                String volumeStr = campVolume.getText().trim();
                String autonomiaStr = campAutonomia.getText().trim();

                if (volumeStr.isEmpty() || autonomiaStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Compila tutti i campi specifici del furgone.",
                        "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int volume = Integer.parseInt(volumeStr);
                int autonomia = Integer.parseInt(autonomiaStr);

                Furgone nuovoFurgone = new Furgone(targa, marca, modello, anno, km,
                        assic, rev, tagl, classeEn, consumo, volume, autonomia);

                if (veicoloDaModificare != null) {
                    g.removeVeicolo(veicoloDaModificare);
                }
                g.addVeicolo(nuovoFurgone);
            }

            dispose(); // chiude il dialogo dopo salvataggio

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Controlla i campi numerici (Posti, Vel. Max, Volume, Autonomia).",
                "Errore formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                "Dati non validi: " + ex.getMessage(),
                "Errore validazione", JOptionPane.ERROR_MESSAGE);
        }
    }
}
