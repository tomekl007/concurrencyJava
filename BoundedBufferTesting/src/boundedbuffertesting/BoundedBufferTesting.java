/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package boundedbuffertesting;

/**
 *
 * @author Tomasz Lelek
 */
public class BoundedBufferTesting {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
       SemaphoreBoundedBuffer sbb = new SemaphoreBoundedBuffer(3);
       sbb.put(new Object());
       sbb.put(new Object());
       sbb.put(new Object());
       sbb.put(new Object());
      

    }
}
