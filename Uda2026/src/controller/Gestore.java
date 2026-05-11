/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.Furgone;
import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author aless
 */
public class Gestore {
    
    private ArrayList<Veicolo> veicoli;
    private File lista;

    public Gestore() {
        veicoli = new ArrayList();ù
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
    
    public void creaFile() throws IOException{
        File csv = new File("veicoli.csv");
        //possibile usare try catch per gestire l'eccezione
        if(csv.createNewFile())
            System.out.println("file creato");
        else System.out.println("file già esistente");
    }

    public void LeggiFile(){
        JFileChooser = fc = new JFileChooser();
        int ris = fc.showOpenDialog(null);

        if(ris == JFileChooser.APPROVE_OPTION){
            lista = fc.getSelectedFile();

            Scanner scanner = new Scanner(lista);
            veicoli.claer();

        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(linea.isEmpty(()) continue;
            String[] = attr = line.split(";");
            if(attr.length > 12)
               //fino a attr[13] Auto a = new Auto(attr[0]);
                
        }
        }



            
    }
}
