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
    
    private ArrayList<Furgone> furgoni;

    public Gestore() {
        furgoni = new ArrayList();
    }

    //getter
    public ArrayList<Furgone> getFurgoni() {
        return (ArrayList<Furgone>)furgoni.clone();
    }
    
    
    public boolean addFurgone(Furgone f){
        if(f == null)
            return false;
        return furgoni.add(f);
    }

    public boolean removeFurgone(Furgone f){
        if(f == null)
            return false;
        return furgoni.remove(f);
    }    
    
    public void creaFile() throws IOException{
        File csv = new File("veicoli.csv");
        //possibile usare try catch per gestire l'eccezione
        if(csv.createNewFile())
            System.out.println("file creato");
        else System.out.println("file già esistente");
    }
}
