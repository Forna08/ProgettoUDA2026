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
import java.io.FileWriter;
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
    
    
    public boolean addVeicolo(Veicolo f){
        if(f == null)
            return false;
        return veicoli.add(f);
    }

    public boolean removeVecolo(Veicolo f){
        if(f == null)
            return false;
        return veicoli.remove(f);
    }    
    
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
    
    public void salvaConNome(String nome){
        File nuovo = new File(nome+".csv");
        JFileChooser fc = new JFileChooser();
        int ris = fc.showOpenDialog(null);
        
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
    }
    
