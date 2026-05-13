package controller;

import java.util.ArrayList;
import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import model.*;

/**
 * Gestore - controller principale dell'applicazione.
 * Contiene tutta la logica di business.
 * Le classi della view chiamano solo i metodi di questa classe.
 *
 * @author Fornacciari Samuele, Berni Alessio
 */
public class Gestore {

    private ArrayList<Veicolo> veicoli;
    private File lista;
    
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Gestore() {
        veicoli = new ArrayList();
        lista = null;
    }

    // getter
    public ArrayList<Veicolo> getVeicoli() {
        return (ArrayList<Veicolo>) veicoli.clone();
    }

    // ------------------------------------------------------------------ //
    //  FILE

    public void creaFile(String nome) throws IOException {
        File csv = new File(nome + ".csv");
        if (csv.createNewFile())
            System.out.println("file creato");
        else
            System.out.println("file già esistente");
    }

    public void apri() {
        JFileChooser fc = new JFileChooser();
        int ris = fc.showOpenDialog(null);

        if (ris == JFileChooser.APPROVE_OPTION) {
            lista = fc.getSelectedFile();
            try {
                Scanner scanner = new Scanner(lista);
                veicoli.clear();

                scanner.nextLine(); // salta la 1° riga (intestazione)

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.isEmpty()) continue;

                    String[] attr = line.split(";");

                    if (attr.length > 12) {
                        Auto a = new Auto(attr[0], attr[1], attr[2],
                                Integer.parseInt(attr[3]), Integer.parseInt(attr[4]),
                                LocalDate.parse(attr[5]), LocalDate.parse(attr[6]),
                                LocalDate.parse(attr[7]), attr[8],
                                Double.parseDouble(attr[9]), Integer.parseInt(attr[10]),
                                attr[11], Integer.parseInt(attr[12]), attr[13]);
                        veicoli.add(a);
                    } else {
                        Furgone f = new Furgone(attr[0], attr[1], attr[2],
                                Integer.parseInt(attr[3]), Integer.parseInt(attr[4]),
                                LocalDate.parse(attr[5]), LocalDate.parse(attr[6]),
                                LocalDate.parse(attr[7]), attr[8],
                                Double.parseDouble(attr[9]), Integer.parseInt(attr[10]),
                                Integer.parseInt(attr[11]));
                        veicoli.add(f);
                    }
                }
                scanner.close();

            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "File non trovato!");
            }
        }
    }

    // BUG RISOLTO: nell'originale salvaConNome usava "lista" invece del file scelto dal JFileChooser
    public void salvaConNome() {
        JFileChooser fc = new JFileChooser();
        int ris = fc.showSaveDialog(null);

        if (ris == JFileChooser.APPROVE_OPTION) {
            lista = fc.getSelectedFile(); // ora usa il file scelto dall'utente
            scriviSuFile(lista);
            JOptionPane.showMessageDialog(null, "File salvato con nome!");
        }
    }

    public void salva() {
        if (lista == null) {
            salvaConNome();
            return;
        }
        scriviSuFile(lista);
        JOptionPane.showMessageDialog(null, "File salvato!");
    }

    // metodo privato per evitare codice duplicato tra salva() e salvaConNome()
    private void scriviSuFile(File file) {
        try {
            PrintWriter pw = new PrintWriter(file);

            // intestazione
            pw.println("targa;marca;modello;anno;km;scadAssicurazione;scadRevisione;"
                     + "scadTagliando;classeEnergetica;consumo;col10;col11;col12;col13");

            for (Veicolo v : veicoli) {
                String base = v.getTarga() + ";" + v.getMarca() + ";" + v.getModello() + ";"
                        + v.getAnno() + ";" + v.getKm() + ";"
                        + v.getScadenzaAssicurazione() + ";"
                        + v.getScadenzaRevisione() + ";"
                        + v.getScadenzaTagliando() + ";"
                        + v.getClasseEnergetica() + ";"
                        + v.getConsumo();

                if (v instanceof Auto) {
                    Auto a = (Auto) v;
                    pw.println(base + ";" + a.getPosti() + ";" + a.getTipo()
                            + ";" + a.getvMax() + ";" + a.getColore());
                } else if (v instanceof Furgone) {
                    Furgone f = (Furgone) v;
                    pw.println(base + ";" + f.getVolume() + ";" + f.getAutonomia());
                }
            }
            pw.close();

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Errore nel salvataggio!");
        }
    }

    public void esci() {
        int scelta = JOptionPane.showConfirmDialog(null, "Uscire?",
                "Conferma uscita", JOptionPane.YES_NO_OPTION);
        if (scelta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // ---------------------------------------------------------------- //
    //  MODIFICA
   
    public boolean addVeicolo(Veicolo v) {
        if (v == null)
            return false;
        return veicoli.add(v);
    }

    public boolean removeVeicolo(Veicolo v) {
        if (v == null)
            return false;
        return veicoli.remove(v);
    }

    // metodi nuovi usati dalla view
    public Veicolo getPerTarga(String targa) {
        for (Veicolo v : veicoli) {
            if (v.getTarga().equals(targa))
                return v;
        }
        return null;
    }

    public boolean eliminaPerTarga(String targa) {
        Veicolo trovato = getPerTarga(targa);
        if (trovato == null)
            return false;
        return veicoli.remove(trovato);
    }

    // riceve i dati grezzi dalla view come array di stringhe,
    // fa il parsing, costruisce l'oggetto e lo aggiunge alla lista
    public boolean inserisci(String[] dati) {
        Veicolo nuovo = costruisciVeicolo(dati);
        if (nuovo == null)
            return false;
        veicoli.add(nuovo);
        return true;
    }

    // sostituisce un veicolo esistente con i nuovi dati
    public boolean modifica(Veicolo vecchio, String[] dati) {
        Veicolo nuovo = costruisciVeicolo(dati);
        if (nuovo == null)
            return false;
        int idx = veicoli.indexOf(vecchio);
        if (idx >= 0)
            veicoli.set(idx, nuovo);
        return true;
    }

    // costruisce un Auto o Furgone a partire dall'array di stringhe
    // tutta la validazione e il parsing stanno qui
    private Veicolo costruisciVeicolo(String[] dati) {
        // controlla che nessun campo sia vuoto
        for (String s : dati) {
            if (s == null || s.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Tutti i campi sono obbligatori.", "Errore", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        try {
            String targa    = dati[0].trim();
            String marca    = dati[1].trim();
            String modello  = dati[2].trim();
            int anno        = Integer.parseInt(dati[3].trim());
            int km          = Integer.parseInt(dati[4].trim());
            LocalDate assic = LocalDate.parse(dati[5].trim());
            LocalDate rev   = LocalDate.parse(dati[6].trim());
            LocalDate tagl  = LocalDate.parse(dati[7].trim());
            String classeEn = dati[8].trim();
            double consumo  = Double.parseDouble(dati[9].trim());

            if (dati.length > 12) {
                int posti       = Integer.parseInt(dati[10].trim());
                String tipoAuto = dati[11].trim();
                int vMax        = Integer.parseInt(dati[12].trim());
                String colore   = dati[13].trim();
                return new Auto(targa, marca, modello, anno, km,
                        assic, rev, tagl, classeEn, consumo,
                        posti, tipoAuto, vMax, colore);
            } else {
                int volume    = Integer.parseInt(dati[10].trim());
                int autonomia = Integer.parseInt(dati[11].trim());
                return new Furgone(targa, marca, modello, anno, km,
                        assic, rev, tagl, classeEn, consumo,
                        volume, autonomia);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "I campi numerici devono essere numeri validi.", "Errore formato", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(null,
                    "Le date devono essere nel formato yyyy-MM-dd (es. 2027-12-31).", "Errore formato", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null,
                    "Dati non validi: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public ArrayList<Veicolo> cerca(String targa, String tipoFiltro) {
        ArrayList<Veicolo> risultati = new ArrayList<>();
        String testoLower = targa.trim().toLowerCase();

        for (Veicolo v : veicoli) {
            boolean targ = testoLower.isEmpty() || v.getTarga().toLowerCase().contains(testoLower);
            boolean tipo  = tipoFiltro.equals("Tutti")
                    || (tipoFiltro.equals("Automobile") && v instanceof Auto)
                    || (tipoFiltro.equals("Furgone") && v instanceof Furgone);

            if (targ && tipo)
                risultati.add(v);
        }
        return risultati;
    }

    // messaggi di dialogo usati dalla view
    public void NessunaSelezione() {
        JOptionPane.showMessageDialog(null, "Seleziona un veicolo dalla lista.",
                "Nessuna selezione", JOptionPane.WARNING_MESSAGE);
    }

    public boolean confermaEliminazione() {
        int risposta = JOptionPane.showConfirmDialog(null,
                "Sei sicuro di voler eliminare il veicolo selezionato?\nL'operazione non può essere annullata.",
                "Conferma eliminazione", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return risposta == JOptionPane.YES_OPTION;
    }

    // ----------------------------------------------------- //
    //  INFO
//AURA 
    public void visualizzaLista() {
        if (veicoli.isEmpty()) {
            JOptionPane.showMessageDialog(null, "La lista è vuota!");
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("AUTO:\n");
        sb.append("Targa;Marca;Modello;Anno;Km;Assicurazione;Revisione;Tagliando;Posti;Tipo;VMax;Colore\n");
        for (Veicolo v : veicoli) {
            if (v instanceof Auto) {
                Auto a = (Auto) v;
                sb.append(a.getTarga() + ";" + a.getMarca() + ";" + a.getModello() + ";"
                        + a.getAnno() + ";" + a.getKm() + ";" + a.getScadenzaAssicurazione() + ";"
                        + a.getScadenzaRevisione() + ";" + a.getScadenzaTagliando() + ";"
                        + a.getPosti() + ";" + a.getTipo() + ";" + a.getvMax() + ";" + a.getColore() + "\n");
            }
        }

        sb.append("\nFURGONI:\n");
        sb.append("Targa;Marca;Modello;Anno;Km;Assicurazione;Revisione;Tagliando;Volume;Autonomia\n");
        for (Veicolo v : veicoli) {
            if (v instanceof Furgone) {
                Furgone f = (Furgone) v;
                sb.append(f.getTarga() + ";" + f.getMarca() + ";" + f.getModello() + ";"
                        + f.getAnno() + ";" + f.getKm() + ";" + f.getScadenzaAssicurazione() + ";"
                        + f.getScadenzaRevisione() + ";" + f.getScadenzaTagliando() + ";"
                        + f.getVolume() + ";" + f.getAutonomia() + "\n");
            }
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JOptionPane.showMessageDialog(null, new JScrollPane(area), "Lista Veicoli", JOptionPane.INFORMATION_MESSAGE);
    }

    public void about() {
        JOptionPane.showMessageDialog(null,
                "Gestionale per una flotta di auto composta da soli furgoni e auto appartenenti ad un'azienda",
                "About", JOptionPane.INFORMATION_MESSAGE);
    }

    public void credits() {
        JOptionPane.showMessageDialog(null,
                "Sviluppata da:\nFornacciari Samuele\nBerni Alessio",
                "Credits", JOptionPane.INFORMATION_MESSAGE);
    }
}
