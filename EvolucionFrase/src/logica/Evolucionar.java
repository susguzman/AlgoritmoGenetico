package logica;

import java.util.LinkedList;

/**
 *
 * @author Jesus
 */
public class Evolucionar {

    private String frase;
    private int size_poblacion, suma_frase, presicion, cruze, mutacion, funcion_objetivo;
    private ListaPresicion poblacion;
    public LinkedList<String> caracteres_frase;
    public int generacion;

    public Evolucionar(String frase, int funcion, int poblacion, int presicion, int cruze, int mutacion) throws Exception {
        this.frase = frase.trim();
        this.size_poblacion = poblacion;
        this.presicion = presicion;
        this.cruze = cruze;
        this.mutacion = mutacion;
        this.generacion = 0;
        this.funcion_objetivo = funcion;

        if (this.frase.isEmpty()) {
            throw new Exception("La frase no puede estar vacia");
        }

        if (!(presicion > 0 && presicion < 101)) {
            throw new Exception("Porcentaje de presicion incorrecto");
        }
        
        if(cruze > poblacion){
            throw new Exception("Los cruzes no pueden se mayores a la poblacion");
        }
        
        if(mutacion > poblacion){
            throw new Exception("Las mutaciones no pueden se mayores a la poblacion");
        }

        analizarFrase();
    }

    public Evolucionar(String frase) throws Exception {
        this(frase, 1, 1000, 95, 100, 200);
    }

    private void analizarFrase() {
        caracteres_frase = new LinkedList<>();
        for (int i = 0; i < frase.length(); i++) {
            int valor = frase.codePointAt(i);
            suma_frase = suma_frase + valor;
            caracteres_frase.add(completarBinario(Integer.toBinaryString(valor)));
        }
        System.out.println("Suma " + suma_frase);
    }

    private void calcularDiferencia(Individuo ind) {
        if (funcion_objetivo == 0) {
            ind.suma = 0;
            if (ind.caracteres != null) {
                for (String caracter : ind.caracteres) {
                    ind.suma = ind.suma + Integer.parseInt(caracter, 2);
                }
            }

            ind.difrencia = Math.abs(ind.suma - ind.padre);
        } else {

            int aciertos = 0;
            if (ind.caracteres != null) {
                for (int i = 0; i < ind.caracteres.size(); i++) {
                    String c1 = caracteres_frase.get(i);
                    String c2 = ind.caracteres.get(i);

                    if (c1.equals(c2)) {
                        aciertos++;
                    }
                }
            }

            ind.difrencia = 100 - ((aciertos * 100) / ind.caracteres.size());
        }
    }

    private void crearProblacion() {
        poblacion = new ListaPresicion();
        for (int i = 0; i < size_poblacion; i++) {
            Individuo ind = new Individuo(suma_frase, frase.length());
            calcularDiferencia(ind);
            poblacion.addPresicion(ind);
        }
    }

    public String getDatosIniciales() {
        StringBuilder sb = new StringBuilder();
        sb.append("INICIO ALGORITMO GENETICO" + "\n\n");
        sb.append("Poblacion inicial: ").append(size_poblacion).append("\n");
        sb.append("Inidividuos a cruzar: ").append(cruze).append("\n");
        sb.append("Inidividuos a Mutar: ").append(mutacion).append("\n");
        sb.append("Presicion de Evolucion (%): ").append(presicion).append("\n\n");

        return sb.toString();
    }

    //Seleccionar los mejores individuos
    private LinkedList<Individuo> opSeleccion() {
        int sel = cruze * 2;
        if (sel >= poblacion.size()) {
            sel = cruze;
        }

        LinkedList<Individuo> seleccionados = new LinkedList<>();
        for (int i = 0; i < sel; i++) {
            seleccionados.add(poblacion.get(i));
        }

        return seleccionados;
    }

    //Cruzar los mejores individuos
    private boolean opCruzar(LinkedList<Individuo> seleccion) {
        int pos_uno = (int) Math.floor(Math.random() * (seleccion.size() - 0 + 0) + 0);
        int pos_dos = (int) Math.floor(Math.random() * (seleccion.size() - 0 + 0) + 0);

        if (pos_uno != pos_dos) {
            Individuo uno = seleccion.get(pos_uno);
            Individuo dos = seleccion.get(pos_dos);

            if (uno != null && dos != null) {
                Individuo ind = new Individuo(uno, dos);
                calcularDiferencia(ind);
                poblacion.addPresicion(ind);
                return true;
            }
        }

        return false;
    }

    //Mutar individuos
    private boolean opMutar() {
        int pos = (int) Math.floor(Math.random() * (poblacion.size() - 0 + 0) + 0);
        Individuo i = poblacion.get(pos);

        if (i != null) {
            if (i.mutar()) {
                calcularDiferencia(i);
                return true;
            }
        }

        return false;
    }

    //Remplazamos para obtener la siguiente generacion
    private void opRemplazo() {
        //Reordenar lista
        ListaPresicion nueva = new ListaPresicion();

        for (int i = 0; i < poblacion.size(); i++) {
            nueva.addPresicion(poblacion.get(i));
        }

        poblacion = nueva;

        //Eliminar peores invididuos
        while (poblacion.size() > size_poblacion) {
            poblacion.removeLast();
        }
    }

    private void generarGeneracion() {
        //Seleecionar
        LinkedList<Individuo> seleccionados = opSeleccion();

        //Cruzar
        int no_cruces = 0;
        while (no_cruces < cruze) {
            if (opCruzar(seleccionados)) {
                no_cruces++;
            }
        }

        //Mutar
        int no_mutados = 0;
        while (no_mutados < mutacion) {
            if (opMutar()) {
                no_mutados++;
            }
        }

        //Remplazar 
        opRemplazo();
    }

    public String getGeneracion() {
        if (generacion == 0) {
            crearProblacion();
        } else {
            generarGeneracion();
        }

        //Imprimir generacion (10 mejores)
        StringBuilder sb = new StringBuilder();
        sb.append("************ GENERACION ").append(generacion).append(" ************\n");
        sb.append("Mejores Inidividuos:").append("\n");

        for (int i = 0; i < 10; i++) {
            Individuo ind = poblacion.get(i);
            sb.append("Individio: ").append(i).append(". Diferencia: ").append(ind.difrencia).append(". Valor: ").append(getSolucion(ind)).append("\n");
        }
        sb.append("\n");

        generacion++;
        return sb.toString();
    }

    public boolean evolucionCompleta() {
        if (poblacion != null) {
            Individuo i = poblacion.getFirst();
            if (i != null) {
                if(funcion_objetivo == 0){
                    return i.difrencia == 0;
                }else{
                    return 100 - i.difrencia >= presicion;
                }
            }
        }

        return false;
    }

    public String getSolucionFinal() {
        if (poblacion != null) {
            Individuo i = poblacion.getFirst();
            return getSolucion(i);
        }

        return "";
    }

    public String getSolucion(Individuo i) {
        if (i != null) {
            String r = "";

            for (String caracter : i.caracteres) {
                char c = (char) Integer.parseInt(caracter, 2);
                r = r + c;
            }

            return r;
        }
        
        return "";
    }

    public static String completarBinario(String binario) {
        if (binario.length() < 8) {
            for (int i = binario.length(); i < 8; i++) {
                binario = "0" + binario;
            }
        }

        return binario;
    }
}
