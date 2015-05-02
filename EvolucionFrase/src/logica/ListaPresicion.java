/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.util.LinkedList;

/**
 *
 * @author Jesus
 */
public class ListaPresicion extends LinkedList<Individuo>{
    
    public void addPresicion(Individuo nuevo){
        boolean agregado = false;
        for (int i = 0; i < this.size() && agregado == false; i++) {
            Individuo ind = this.get(i);
            
            if(ind.difrencia >= nuevo.difrencia){
                this.add(i, nuevo);
                agregado = true;
            }
        }
        
        if(agregado == false){
            this.addLast(nuevo);
        }        
    }
    
}
