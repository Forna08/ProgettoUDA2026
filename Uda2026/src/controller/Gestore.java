/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import model.*;
/**
 *
 * @author aless
 */
public class Gestore {
    
    private ArrayList<Veicolo> veicoli;
    private File lista;

    public Gestore() {
        veicoli = new ArrayList();
        lista = null;
    }

    //getter
    public ArrayList<Veicolo> getVeicoli() {
        return (ArrayList<Veicolo>)veicoli.clone();
    }  
 
//......FILE....... 
    public void creaFile(String nome) throws IOException{
        File csv = new File(nome+".csv");
        //possibile usare try catch per gestire l'eccezione
        if(csv.createNewFile())
            System.out.println("file creato");
        else System.out.println("file già esistente");
    }

    public void apri(){
        JFileChooser fc = new JFileChooser();
        int ris = fc.showOpenDialog(null);

        if(ris == JFileChooser.APPROVE_OPTION){
                lista = fc.getSelectedFile();
            try {    
                Scanner scanner = new Scanner(lista);
                veicoli.clear();
                
                scanner.nextLine();//salta la 1 riga
                
                while(scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    if(line.isEmpty()) continue;
                    String[] attr = line.split(";"); //spezza la linea in un array, che poi verrà letto per aggiungere gli attributi delle classi sul csv
                    if(attr.length > 12){
                        Auto a = new Auto(attr[0], attr[1], attr[2], Integer.parseInt(attr[3]),
                                Integer.parseInt(attr[4]), LocalDate.parse(attr[5]),
                                LocalDate.parse(attr[6]),LocalDate.parse(attr[7]), attr[8],
                                Double.parseDouble(attr[9]), Integer.parseInt(attr[10]),
                                attr[11], Integer.parseInt(attr[12]), attr[13]);
                        veicoli.add(a);
                    }
                    else { Furgone f = new Furgone(attr[0], attr[1], attr[2], Integer.parseInt(attr[3]),
                            Integer.parseInt(attr[4]), LocalDate.parse(attr[5]),
                            LocalDate.parse(attr[6]),LocalDate.parse(attr[7]), attr[8],
                            Double.parseDouble(attr[9]), Integer.parseInt(attr[10]), Integer.parseInt(attr[11]));
                    veicoli.add(f);
                    }
                }
                scanner.close();
            } catch (FileNotFoundException ex) {
                 JOptionPane.showMessageDialog(null, "File non trovato!");
            }
        }
    }
    
    public void salvaConNome(){
        JFileChooser fc = new JFileChooser();
        int ris = fc.showSaveDialog(null);
        
        if (ris == JFileChooser.APPROVE_OPTION) {
            try {
            PrintWriter pw = new PrintWriter(lista);

            for (Veicolo v : veicoli) {
                pw.println(v.getTarga()+";"+v.getMarca()+";"+v.getModello()+";"+
                        v.getAnno()+";"+v.getKm()+";"+v.getScadenzaAssicurazione()+";"+
                        v.getScadenzaRevisione()+";"+v.getScadenzaTagliando());
            }
            pw.close();
            JOptionPane.showMessageDialog(null, "File salvato con nome!");

            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Errore nel salvataggio!");
            }
        }
    }
    
    
    public void salva() {
        if (lista == null) {
            salvaConNome();
            return;
        }
    try {
        PrintWriter pw = new PrintWriter(lista);

        for (Veicolo v : veicoli) {
            pw.print(v.getTarga()+";"+v.getMarca()+";"+v.getModello()+";"+
                        v.getAnno()+";"+v.getKm()+";"+v.getScadenzaAssicurazione()+";"+
                        v.getScadenzaRevisione()+";"+v.getScadenzaTagliando());
            if(v instanceof Auto){
                pw.println(((Auto) v).getPosti()+";"+((Auto) v).getTipo()+";"+
                        ((Auto) v).getvMax()+((Auto) v).getColore());
            } else if(v instanceof Furgone){
                pw.println(((Furgone) v).getVolume()+";"+((Furgone) v).getAutonomia());
            }
        }
        pw.close();
        JOptionPane.showMessageDialog(null, "File salvato!");

    } catch (FileNotFoundException e) {
        JOptionPane.showMessageDialog(null, "Errore nel salvataggio!");
        }
    }
    
    public void esci() {
        int scelta = JOptionPane.showConfirmDialog(
            null,
            "Uscire?",
            "Conferma uscita",
            JOptionPane.YES_NO_OPTION
        );

        if (scelta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }    
//......MODIFICA......
    
    public boolean addVeicolo(Veicolo v){
        if(v == null)
            return false;
        
        return veicoli.add(v);
    }
    
    public boolean removeVeicolo(Veicolo v){
        if(v == null)
            return false;
        
        return veicoli.remove(v);
    }   
    
    public void visualizzaLista() {//visualizza prima le auto poi i furgoni
        if (veicoli.isEmpty()) {
            JOptionPane.showMessageDialog(null, "La lista è vuota!");
            return;
        }

        StringBuilder sb = new StringBuilder();

        // auto
        sb.append("AUTO:\n");
        sb.append("Targa;Marca;Modello;Anno;Km;ScadenzaAssicurazione;ScadenzaRevisione;ScadenzaTagliando;Posti;Tipo;VelocitàMax;Colore\n");

        for (Veicolo v : veicoli) {
            if (v instanceof Auto) {
                Auto a = (Auto) v;
                sb.append(a.getTarga()+";"+a.getMarca()+";"+a.getModello()+";"+
                          a.getAnno()+";"+a.getKm()+";"+a.getScadenzaAssicurazione()+";"+
                          a.getScadenzaRevisione()+";"+a.getScadenzaTagliando()+";"+
                          a.getPosti()+";"+a.getTipo()+";"+a.getvMax()+";"+a.getColore()+"\n");
            }
        }

        // furgoni
        sb.append("\nFURGONI:\n");
        sb.append("Targa;Marca;Modello;Anno;Km;ScadenzaAssicurazione;ScadenzaRevisione;ScadenzaTagliando;Volume;Autonomia\n");

        for (Veicolo v : veicoli) {
            if (v instanceof Furgone) {
                Furgone f = (Furgone) v;
                sb.append(f.getTarga()+";"+f.getMarca()+";"+f.getModello()+";"+
                          f.getAnno()+";"+f.getKm()+";"+f.getScadenzaAssicurazione()+";"+
                          f.getScadenzaRevisione()+";"+f.getScadenzaTagliando()+";"+
                          f.getVolume()+";"+f.getAutonomia()+"\n");
            }
        }
    }
          
//..... INFO ......
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
    
