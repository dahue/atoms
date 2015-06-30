/* Trabajo Práctico N°2 - Atoms
 * Autores:
 *          Gustavo Martinez
 *          Adrian Tissera
 */
package p02e01;

import stateSpace.AdversarySearchState;
import util.Pair;

/**
 * Clase que implementa un estado del problema Atoms
 */
public class AtomsState implements AdversarySearchState {

    private static final int WIDTH = 10;//10
    private static final int HEIGHT = 6;//6
    private boolean max;
    private int[][] board;
    private Object ruleApplied;
    
    /**
     * Constructor por defecto
     */
    public AtomsState() {
        this.board = new int[HEIGHT][WIDTH];
        this.ruleApplied = new Pair<>();
        this.max = false;
    }

    /**
     * Constructor que inicializa el estado con el arreglo board
     * @param board es el arreglo que se usa para inicializar
     */
    public AtomsState(int[][] board) {
        this.board = board;
        max = false;
    }
    
    /**
     * Dice si el estado es MAX
     * @return devuelve true si 'this' es MAX
     */
    @Override
    public boolean isMax() {
        return max;
    }
    
    /**
     * Setea el estado con true si es MAX
     * @param max es el valor con que se setea 'this.max'
     */
    public void setMax(boolean max) {
        this.max = max;
    }
    
    /**
     * Devuelve true si 'this' es igual al estado 'other'
     * @param other es un estado del problema
     * @return devuelve true si 'this' es igual al estado 'other'
     */
    @Override
    public boolean equals(AdversarySearchState other) {
        AtomsState other_ = (AtomsState) other;
        boolean result = true;
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                if (this.board[i][j] != other_.board[i][j]) {
                    return false;
                }
            }
        }
        return result;
    }
    
    /**
     * Dice cual fue la regla aplicada
     * @return devuelve un par con la posición donde se colocó un atomo
     */
    @Override
    public Object ruleApplied() {
        return (Pair<Integer, Integer>) this.ruleApplied;
    }

    /**
     * Setea la regla que se aplico para obtener el estado 'this'
     * @param position es par con la posición del atomo colocado
     */
    public void setRuleApplied(Pair<Integer, Integer> position) {
        this.ruleApplied = position;
    }
    
    /**
     * 
     * @return devuelve un string con la representación del estado 'this'
     */
    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i < getHeight(); i++) {
            string += "[";
            for (int j = 0; j < getWidth(); j++) {
                string += this.board[i][j] + ",";
            }
            string += "]\n";
        }
        return string;
    }
    
    /**
     * 
     * @return devuelve el arreglo que contiene el tablero de juego y sus
     * correspondientes atomos en él
     */
    public int[][] getBoard() {
        return board;
    }
    
    /**
     * Setea el tablero de juego con un estado determinado
     * @param board es el arreglo con las posiciones de los atomos si los hay
     */
    public void setBoard(int[][] board) {
        this.board = board;
    }

    /**
     * Setea el tablero de juego con una copia del estado pasado como parametro
     * @param board es el tablero que se usa como referencia para copiar
     */
    public void setBoardCopy(int[][] board) {
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }

    /**
     * @return the WIDTH
     */
    public static int getWidth() {
        return WIDTH;
    }

    /**
     * @return the HEIGHT
     */
    public static int getHeight() {
        return HEIGHT;
    }
}
