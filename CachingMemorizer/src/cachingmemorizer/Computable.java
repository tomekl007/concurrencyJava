/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cachingmemorizer;

/**
 *
 * @author Tomasz Lelek
 */
interface Computable <A, V> {
    V compute(A arg) throws InterruptedException;
}