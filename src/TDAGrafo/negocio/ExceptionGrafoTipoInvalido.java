/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TDAGrafo.negocio;

/**
 *
 * @author HP
 */
public class ExceptionGrafoTipoInvalido extends Exception {

    /**
     * Creates a new instance of <code>ExceptionGrafoTipoInvalido</code> without
     * detail message.
     */
    public ExceptionGrafoTipoInvalido() {
    }

    /**
     * Constructs an instance of <code>ExceptionGrafoTipoInvalido</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ExceptionGrafoTipoInvalido(String msg) {
        super(msg);
    }
}
