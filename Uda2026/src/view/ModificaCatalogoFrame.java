package view;

import controller.Gestore;
import java.awt.*;
import javax.swing.*;
import model.*;

/**
 * ModificaCatalogoFrame - finestra modale per inserire o modificare un veicolo.
 * Raccoglie i dati dai campi e li passa al Gestore come array di stringhe.
 * Non contiene logica di business: niente parsing, niente validazione, niente costruzione oggetti.
 *
 * @author Fornacciari Samuele, Berni Alessio
 */
public class ModificaCatalogoFrame extends JDialog {

    private final Gestore g;
    private final Veicolo veicoloDaModificare; // null = inserimento

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

    /**
     * Costruttore.
     *
     * @param parent finestra padre
     * @param g      gestore condiviso
     * @param v      veicolo da modificare, null per inserimento
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
     * Costruisce tutti i componenti grafici della finestra.
     */
    private void initComponents() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(430, 570);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel pannelloPrincipale = new JPanel();
        pannelloPrincipale.setLayout(new BoxLayout(pannelloPrincipale, BoxLayout.Y_AXIS));
        pannelloPrincipale.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // campi comuni
        campTarga            = new JTextField(10);
        comboTipo            = new JComboBox<>(new String[]{"Automobile", "Furgone"});
        campMarca            = new JTextField(10);
        campModello          = new JTextField(10);
        campAnno             = new JTextField(10);
        campKm               = new JTextField(10);
        campAssicurazione    = new JTextField(10);
        campRevisione        = new JTextField(10);
        campTagliando        = new JTextField(10);
        campClasseEnergetica = new JTextField(10);
        campConsumo          = new JTextField(10);

        pannelloPrincipale.add(creaRiga("Targa:",                            campTarga));
        pannelloPrincipale.add(creaRiga("Tipo:",                             comboTipo));
        pannelloPrincipale.add(creaRiga("Marca:",                            campMarca));
        pannelloPrincipale.add(creaRiga("Modello:",                          campModello));
        pannelloPrincipale.add(creaRiga("Anno:",                             campAnno));
        pannelloPrincipale.add(creaRiga("Km:",                               campKm));
        pannelloPrincipale.add(creaRiga("Scad. Assicurazione (yyyy-MM-dd):", campAssicurazione));
        pannelloPrincipale.add(creaRiga("Scad. Revisione (yyyy-MM-dd):",     campRevisione));
        pannelloPrincipale.add(creaRiga("Scad. Tagliando (yyyy-MM-dd):",     campTagliando));
        pannelloPrincipale.add(creaRiga("Classe Energetica:",                campClasseEnergetica));
        pannelloPrincipale.add(creaRiga("Consumo (l/100km):",                campConsumo));

        // pannello Auto
        campPosti    = new JTextField(10);
        campTipoAuto = new JTextField(10);
        campVMax     = new JTextField(10);
        campColore   = new JTextField(10);

        pannelloAuto = new JPanel();
        pannelloAuto.setLayout(new BoxLayout(pannelloAuto, BoxLayout.Y_AXIS));
        pannelloAuto.setBorder(BorderFactory.createTitledBorder("Dati Auto"));
        pannelloAuto.add(creaRiga("Posti:",                 campPosti));
        pannelloAuto.add(creaRiga("Tipo (es. berlina):",    campTipoAuto));
        pannelloAuto.add(creaRiga("Vel. Max (km/h):",       campVMax));
        pannelloAuto.add(creaRiga("Colore:",                campColore));

        // pannello Furgone
        campVolume    = new JTextField(10);
        campAutonomia = new JTextField(10);

        pannelloFurgone = new JPanel();
        pannelloFurgone.setLayout(new BoxLayout(pannelloFurgone, BoxLayout.Y_AXIS));
        pannelloFurgone.setBorder(BorderFactory.createTitledBorder("Dati Furgone"));
        pannelloFurgone.add(creaRiga("Volume (m³):",    campVolume));
        pannelloFurgone.add(creaRiga("Autonomia (km):", campAutonomia));

        pannelloPrincipale.add(pannelloAuto);
        pannelloPrincipale.add(pannelloFurgone);

        comboTipo.addActionListener(e -> aggiornaPannelloTipo());
        aggiornaPannelloTipo();

        // pulsanti
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
     * Crea una riga etichetta + campo.
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
     * Pre-popola i campi con i dati del veicolo esistente (modalita' modifica).
     *
     * @param v veicolo da cui leggere i dati
     */
    private void prePopola(Veicolo v) {
        campTarga.setText(v.getTarga());
        campTarga.setEditable(false);
        campMarca.setText(v.getMarca());
        campModello.setText(v.getModello());
        campAnno.setText(String.valueOf(v.getAnno()));
        campKm.setText(String.valueOf(v.getKm()));
        campAssicurazione.setText(v.getScadenzaAssicurazione().toString());
        campRevisione.setText(v.getScadenzaRevisione().toString());
        campTagliando.setText(v.getScadenzaTagliando().toString());
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
        comboTipo.setEnabled(false);
        aggiornaPannelloTipo();
    }

    /**
     * Raccoglie le stringhe dai campi e le passa al Gestore.
     * Niente parsing, niente validazione: ci pensa il Gestore.
     * Se il Gestore restituisce true, chiude la finestra.
     */
    private void conferma() {
        boolean isAuto = comboTipo.getSelectedItem().equals("Automobile");
        boolean ok;

        if (isAuto) {
            // Auto: 14 campi
            String[] dati = {
                campTarga.getText(),
                campMarca.getText(),
                campModello.getText(),
                campAnno.getText(),
                campKm.getText(),
                campAssicurazione.getText(),
                campRevisione.getText(),
                campTagliando.getText(),
                campClasseEnergetica.getText(),
                campConsumo.getText(),
                campPosti.getText(),
                campTipoAuto.getText(),
                campVMax.getText(),
                campColore.getText()
            };

            if (veicoloDaModificare == null) {
                ok = g.inserisci(dati);
            } else {
                ok = g.modifica(veicoloDaModificare, dati);
            }

        } else {
            // Furgone: 12 campi
            String[] dati = {
                campTarga.getText(),
                campMarca.getText(),
                campModello.getText(),
                campAnno.getText(),
                campKm.getText(),
                campAssicurazione.getText(),
                campRevisione.getText(),
                campTagliando.getText(),
                campClasseEnergetica.getText(),
                campConsumo.getText(),
                campVolume.getText(),
                campAutonomia.getText()
            };

            if (veicoloDaModificare == null) {
                ok = g.inserisci(dati);
            } else {
                ok = g.modifica(veicoloDaModificare, dati);
            }
        }

        // chiude solo se il Gestore ha confermato il successo
        if (ok) {
            dispose();
        }
    }
}
