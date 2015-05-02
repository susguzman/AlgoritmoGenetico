/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Jesus
 */
public class Individuo {

    public double difrencia;
    public int suma, padre;
    public LinkedList<String> caracteres;
    public ArrayList<String> lista;

    public Individuo(int padre, int cantidad) {
        caracteres = new LinkedList<>();
        this.padre = padre;

        generarIndividuo(cantidad);
    }

    public Individuo(Individuo uno, Individuo dos) {
        this.padre = uno.padre;
        crearHijo(uno, dos);
    }

    private void generarIndividuo(int cantidad) {
        if (caracteres != null) {
            for (int i = 0; i < cantidad; i++) {
                int valor = (int) Math.floor(Math.random() * (126 - 32 + 1) + 32);
                caracteres.add(Evolucionar.completarBinario(Integer.toBinaryString(valor)));
            }
        }
    }

    public boolean mutar() {
        int intentos = 0;

        while (intentos < 20) {
            int pos_caracter = (int) Math.floor(Math.random() * (caracteres.size() - 0 + 0) + 0);
            String caracter = caracteres.get(pos_caracter);

            if (caracter != null) {
                int bit = (int) Math.floor(Math.random() * (7 - 0 + 1) + 0);
                char bits[] = caracter.toCharArray();

                //Mutar bit
                if (bits[bit] == '1') {
                    bits[bit] = '0';
                } else {
                    bits[bit] = '1';
                }

                //Nuevo caracter
                String nuevo_caracter = new String(bits);
                int valor_nuevo = Integer.parseInt(nuevo_caracter, 2);

                if (valor_nuevo > 31 && valor_nuevo < 127) {

                    //Agregar nuevo caracter
                    caracteres.remove(pos_caracter);
                    caracteres.add(pos_caracter, nuevo_caracter);

                    return true;
                }
            }

            intentos++;
        }

        return false;
    }

    private void crearHijo(Individuo uno, Individuo dos) {
        //Calculando cantidades        
        int cantidad_uno = (int) Math.floor(Math.random() * (uno.caracteres.size() - 0 + 1) + 0);
        if (cantidad_uno == uno.caracteres.size()) {
            cantidad_uno = cantidad_uno - 1;
        }

        //Crear hijo
        caracteres = new LinkedList<>();

        //Herencia de uno
        for (int i = 0; i < cantidad_uno; i++) {
            caracteres.add(uno.caracteres.get(i));
        }

        //Herencia de dos
        for (int i = cantidad_uno; i < uno.caracteres.size(); i++) {
            caracteres.add(dos.caracteres.get(i));
        }
    }
}
