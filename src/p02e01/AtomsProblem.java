/* Trabajo Práctico N°2 - Atoms
 * Autores:
 *          Gustavo Martinez
 *          Adrian Tissera
 */
package p02e01;

import static java.lang.Math.*;
import java.util.LinkedList;
import java.util.List;
import stateSpace.AdversarySearchProblem;
import util.Pair;

/**
 * Clase que implementa el problema Atoms
 */
public class AtomsProblem implements AdversarySearchProblem<AtomsState> {
    private final AtomsState initialState;
    private final int MAX_ATOMS = (AtomsState.getHeight() - 2) * (AtomsState.getWidth() - 2) * 3
                                + (AtomsState.getHeight() - 2) * 2 * 2
                                + (AtomsState.getWidth() - 2) * 2 * 2 + 4;
    private final int minValue;
    private final int maxValue;

    /**
     * Constructor por defecto
     */
    public AtomsProblem() {
        this.initialState = new AtomsState();
        minValue = MAX_ATOMS * (-1);
        maxValue = MAX_ATOMS;
    }

    /**
     * Constructor parametrizado, inicializa el problema con un estado inicial
     * pasado como parametro
     * @param initialState 
     */
    public AtomsProblem(AtomsState initialState) {
        this.initialState = initialState;
        minValue = MAX_ATOMS * (-1);
        maxValue = MAX_ATOMS;
    }

    /**
     * @return devuelve el estado inicial del problema
     */
    @Override
    public AtomsState initialState() {
        return this.initialState;
    }

    /**
     * @param state es el estado del cual se quieren conocer los sucesores
     * @return devuelve la lista de los sucesores de un estado 'state'
     */
    @Override
    public List<AtomsState> getSuccessors(AtomsState state) {
        List<AtomsState> successors = new LinkedList();
        if (this.end(state)) {
            return successors;
        } else {
            int value;
            for (int i = 0; i < AtomsState.getHeight(); i++) {
                for (int j = 0; j < AtomsState.getWidth(); j++) {

                    AtomsState successor = new AtomsState();
                    successor.setBoardCopy(state.getBoard());
                    successor.setMax(state.isMax());
                    if (this.isValid(i, j, successor)) {
                        successor = addAtom(i, j, successor);
                        if (!state.equals(successor)) {
                            Pair<Integer, Integer> position = new Pair(i, j);
                            successor.setRuleApplied(position);
                            successor.setMax(!successor.isMax());
                            successors.add(successor);
                        }
                    }
                }//endfor j
            }//endfor i
            return successors;
        }
    }

    /**
     * Agrega un átomo en una posición determinada
     * @param x es la componente 'x' de la posición del átomo a colocar
     * @param y es la componente 'y' de la posición del átomo a colocar
     * @param state es el estado al cual se le agregará un átomo
     * @return devuelve el estado 'state' con un nuevo átomo
     */
    public AtomsState addAtom(int x, int y, AtomsState state) {
        if (this.end(state)) {
            return state;
        } else {
            int p; // p for player
            if (x < 0 || y < 0 || x > AtomsState.getHeight() - 1 || y > AtomsState.getWidth() - 1) {

                return state;
            } else {
                int[][] board = state.getBoard();
                if (board[x][y] < 1 && board[x][y] > -1) {
                    board[x][y] = (state.isMax()) ? (board[x][y] + 1) : (board[x][y] - 1);
                    state.setBoard(board);
                } else if (isACorner(x, y)) {
                    board[x][y] = 0;
                    addAtom(x - 1, y, state);//N
                    addAtom(x + 1, y, state);//S
                    addAtom(x, y - 1, state);//E
                    addAtom(x, y + 1, state);//W
                } else if (board[x][y] < 2 && board[x][y] > -2) {
                    if (board[x][y] > 0) {
                        board[x][y] = (state.isMax()) ? (board[x][y] + 1) : (board[x][y] * -1) - 1;
                    } else if (board[x][y] < 0) {
                        board[x][y] = (state.isMax()) ? (board[x][y] * -1 + 1) : (board[x][y]) - 1;
                    }
                    state.setBoard(board);
                } else if (isAnEdge(x, y)) {
                    board[x][y] = 0;
                    addAtom(x - 1, y, state);//N
                    addAtom(x + 1, y, state);//S
                    addAtom(x, y - 1, state);//E
                    addAtom(x, y + 1, state);//W
                } else if (board[x][y] < 3 && board[x][y] > -3) {
                    if (board[x][y] > 0) {
                        board[x][y] = (state.isMax()) ? (board[x][y] + 1) : (board[x][y] * -1) - 1;
                    } else if (board[x][y] < 0) {
                        board[x][y] = (state.isMax()) ? (board[x][y] * -1 + 1) : (board[x][y]) - 1;
                    }
                    state.setBoard(board);
                } else {
                    board[x][y] = 0;
                    addAtom(x - 1, y, state);//N
                    addAtom(x + 1, y, state);//S
                    addAtom(x, y - 1, state);//E
                    addAtom(x, y + 1, state);//W
                }
                return state;
            }//end else
        }
    }

    /**
     * Dice si se puede colocar un átomo en la posición (x,y)
     * @param x es la componente 'x' de la posición del átomo a colocar
     * @param y es la componente 'y' de la posición del átomo a colocar
     * @param state es el estado donde se pretende colocar un átomo
     * @return devuelve true si se puede colocar un átomo en la posición (x,y)
     */
    public boolean isValid(int x, int y, AtomsState state) {
        boolean condition = state.getBoard()[x][y] >= 0 && state.isMax();
        condition = condition || state.getBoard()[x][y] <= 0 && !state.isMax();
        return condition;
    }

    /**
     * @param x es la componente 'x' de la posición un átomo
     * @param y es la componente 'y' de la posición un átomo
     * @return devuelve true si la posicion (x,y) es una esquina
     */ 
    private static boolean isACorner(int x, int y) {
        boolean condition = (x == 0 && y == 0) || (x == AtomsState.getHeight()-1 && y == AtomsState.getWidth()-1);
        condition = condition || (x == 0 && y == AtomsState.getWidth()-1) || (x == AtomsState.getHeight()-1 && y == 0);
        return condition;
    }

    /**
     * @param x es la componente 'x' de la posición un átomo
     * @param y es la componente 'y' de la posición un átomo
     * @return devuelve true si la posicion (x,y) es un borde que no es esquina
     */
    private static boolean isAnEdge(int x, int y) {
        boolean condition = (x > 0 && x < AtomsState.getHeight()-1 && y == 0) || (x > 0 && x < AtomsState.getHeight()-1 && y == AtomsState.getWidth()-1);
        condition = condition || (x == 0 && y > 0 && y < AtomsState.getWidth()-1) || (x == AtomsState.getHeight()-1 && y > 0 && y < AtomsState.getWidth()-1);
        return condition;
    }

    /**
     * Dice si un estado es final
     * @param state es el estado a evaluar
     * @return devuelve true si 'state' es un estado final
     */
    @Override
    public boolean end(AtomsState state) {
        boolean max = state.isMax();
        boolean end = true;
        int countAtoms = 0;
        for (int i = 0; i < AtomsState.getHeight(); i++) {
            for (int j = 0; j < AtomsState.getWidth(); j++) {
                if (max) {
                    end = end && state.getBoard()[i][j] >= 0;
                } else {
                    end = end && state.getBoard()[i][j] <= 0;
                }
                countAtoms += abs(state.getBoard()[i][j]);
            }
        }
        return end;
    }

    /**
     * Devuelve la valoración para un estado en particular si es una hoja
     * @param state es el estado a evaluar
     * @return devuelve la diferencia de atomos entre min y max
     */
    @Override
    public int value(AtomsState state) {
        boolean max = state.isMax();
        int maxAtoms = 0;
        int minAtoms = 0;
        for (int i = 0; i < AtomsState.getHeight(); i++) {
            for (int j = 0; j < AtomsState.getWidth(); j++) {
                if (state.getBoard()[i][j] > 0) {
                    maxAtoms += state.getBoard()[i][j];
                } else {
                    minAtoms += state.getBoard()[i][j];
                }
            }
        }
        return maxAtoms - abs(minAtoms);
    }

    /**
     * @return devuelve la maxima cantidad de átomos que puede tener min
     */
    @Override
    public int minValue() {
        return minValue;
    }

    /**
     * 
     * @return devuelve la maxima cantidad de átomos que puede tener max
     */
    @Override
    public int maxValue() {
        return maxValue;
    }
}
