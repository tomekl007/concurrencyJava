/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parralezingrecursivealgoritms;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import parralezingrecursivealgoritms.TransformingSequential.Element;
import parralezingrecursivealgoritms.TransformingSequential.Node;
import parralezingrecursivealgoritms.TransformingSequential.NodeImpl;

/**
 *
 * @author Tomasz Lelek
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        List<Element> el = new LinkedList<>();
        el.add(new Element() {});
        el.add(new Element() {});

        TransformingSequential ts = new TransformingSequential() {

            @Override
            public void process(Element e) {
                System.out.println("some processing in " +Thread.currentThread().toString() );
            }
        };
        
        ts.processSequentially(el);
        ExecutorService exec = Executors.newCachedThreadPool();
        ts.processInParallel(exec, el);
        
        //----------------------------------
        
        Node tree = ts.new NodeImpl();
        List<Node<String>> childrens = tree.getChildren();
        childrens.add(ts.new NodeImpl());
        tree.getChildren().add(ts.new NodeImpl());
        List<Node<String>> nodes = new LinkedList<>();
        nodes.add(tree);
        
        Collection<String> result = new LinkedList<>();
        ts.sequentialRecursive(nodes, result);
        System.out.println(result);
        
       // ts.parallelRecursive(exec, nodes, result);
//        Thread.sleep(1000);
//         System.out.println(result);
//     
        Collection<String> res = ts.getParallelResults(nodes);
      //  Collection<String> res2 =  ts.getParallelResults(nodes);
        System.out.println(res);
            //    System.out.println(res2);

        
        
        
        
        
    }
}
