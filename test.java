import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.lang.System.*;
class Resource{
    public int instance; 
    Resource(int instance) 
    { 
        this.instance = instance; 
    }
    boolean isAvailable()
    {
        if(this.instance>0)
        {return true;}
        return false;
    }
    
    void decreaseRes()
    {
        this.instance--;
    }
    void increaseRes()
    {
        this.instance++;
    }
}

class Digraph<V> {
 
    private Map<V,List<V>> neighbors = new HashMap<V,List<V>>();

    public String toString () {
        StringBuffer s = new StringBuffer();
        for (V v: neighbors.keySet()) s.append("\n    " + v + " -> " + neighbors.get(v));
        return s.toString();                
    }

    public void add (V vertex) {
        if (neighbors.containsKey(vertex)) return;
        neighbors.put(vertex, new ArrayList<V>());
    }

    public boolean contains (V vertex) {
        return neighbors.containsKey(vertex);
    }

    public boolean isEdge(V from, V to)
    {
    	if(this.contains(from) && this.contains(to))
    	{
    		return true;
    	}
    	return false;
	    }

    public void add (V from, V to) {
        this.add(from); this.add(to);
        neighbors.get(from).add(to);
    }

    public void remove (V from, V to) {
        if (!(this.contains(from) && this.contains(to)))
            {}//throw new IllegalArgumentException("Nonexistent vertex");
        else{neighbors.get(from).remove(to);}
    }

    public Map<V,Integer> outDegree () {
        Map<V,Integer> result = new HashMap<V,Integer>();
        for (V v: neighbors.keySet()) result.put(v, neighbors.get(v).size());
        return result;
    }

    public Map<V,Integer> inDegree () {
        Map<V,Integer> result = new HashMap<V,Integer>();
        for (V v: neighbors.keySet()) result.put(v, 0);       // All in-degrees are 0
        for (V from: neighbors.keySet()) {
            for (V to: neighbors.get(from)) {
                result.put(to, result.get(to) + 1);           // Increment in-degree
            }
        }
        return result;
    }

    public List<V> topSort () {
        Map<V, Integer> degree = inDegree();
        // Determine all vertices with zero in-degree
        Stack<V> zeroVerts = new Stack<V>();        // Stack as good as any here
        for (V v: degree.keySet()) {
            if (degree.get(v) == 0) zeroVerts.push(v);
        }
        // Determine the topological order
        List<V> result = new ArrayList<V>();
        while (!zeroVerts.isEmpty()) {
            V v = zeroVerts.pop();                  // Choose a vertex with zero in-degree
            result.add(v);                          // Vertex v is next in topol order
            // "Remove" vertex v by updating its neighbors
            for (V neighbor: neighbors.get(v)) {
                degree.put(neighbor, degree.get(neighbor) - 1);
                // Remember any vertices that now have zero in-degree
                if (degree.get(neighbor) == 0) zeroVerts.push(neighbor);
            }
        }
        // Check that we have used the entire graph (if not, there was a cycle)
        if (result.size() != neighbors.size()) return null;
        return result;
    }

    public boolean isDag () {
        return topSort() != null;
    }

    public Map bfsDistance (V start) {
        Map<V,Integer> distance = new HashMap<V,Integer>();
        // Initially, all distance are infinity, except start node
        for (V v: neighbors.keySet()) distance.put(v, null);
        distance.put(start, 0);
        // Process nodes in queue order
        Queue<V> queue = new LinkedList<V>();
        queue.offer(start);                                    // Place start node in queue
        while (!queue.isEmpty()) {
            V v = queue.remove();
            int vDist = distance.get(v);
            // Update neighbors
            for (V neighbor: neighbors.get(v)) {
                if (distance.get(neighbor) != null) continue;  // Ignore if already done
                distance.put(neighbor, vDist + 1);
                queue.offer(neighbor);
            }
        }
        return distance;
    }
    
}

class cSection{
    Resource[] rResource={new Resource(1),new Resource(1)};
	Digraph<Integer> graph = new Digraph<Integer>();
    int counter=0,flag=-1;
    long endTimeMillis = System.currentTimeMillis() + 2000;
    long deadlockcallTimeMillis = System.currentTimeMillis() + 1000;
    int got_res1,got_res2;
    private class solve extends Thread
    {
      public void run()
      {
        synchronized(graph)
        {
        System.out.println("Solver got this graph "+graph);
        if(graph.isEdge(0,2)==true)
        {
            rResource[0].increaseRes();
            graph.remove(0,2);
            got_res1--;
        }
        else if(graph.isEdge(0,3)==true)
        {
            rResource[0].increaseRes();
            graph.remove(0,3);
            got_res2--;
        }
        else if(graph.isEdge(1,2)==true)
        {
            rResource[1].increaseRes();
            graph.remove(1,2);
            got_res1--;
        }
        else if(graph.isEdge(1,3)==true)
        {
            rResource[1].increaseRes();
            graph.remove(0,2);
            got_res2--;
        }
        }

      }  
    }

    private class iTh extends Thread{ 
       // Thread I= new iTh();
        //Thread J = new jTh();
        public void run(){
     //   System.out.println(System.currentTimeMillis());
        if(System.currentTimeMillis()<endTimeMillis)
                {try{
                    while(counter<3 && System.currentTimeMillis()<endTimeMillis)
                    {
                        got_res1=0;
                        while(got_res1!=2 && System.currentTimeMillis()<endTimeMillis)
                        {
                            if(rResource[0].isAvailable()==true)
                            {
                                rResource[0].decreaseRes();
                                got_res1++;
                              	graph.remove(2,0);
                              	if(graph.isEdge(0,2)!=true)
                                graph.add(0,2);
                       //         graph.add(0,2);
                            }
                            else {
                            	if(graph.isEdge(2,0)!=true)
                            	graph.add(2,0);
                            }
                            if(rResource[1].isAvailable()==true)
                            {
                                rResource[1].decreaseRes();
                                got_res1++;
                                graph.remove(2,1);
                                if(graph.isEdge(1,2)!=true)
                                graph.add(1,2);
                            }
                            else{
                            	if(graph.isEdge(2,1)!=true)
                            	graph.add(2,1);
                            }
                        if(graph.isDag()!=true)
              			{
                            flag=1;
                        }  
                        if(flag==1){
                            Thread.sleep(500);                    //      I.suspend();
                            //suspend();
                            Thread solve=new solve();
                            solve.start();
                           //try{solve.join();}catch(Exception ex){}
                          // resume();flag=0;
                           // I.join();
                           // J.join();
                           //System.out.println("The output graph: " + graph); 
                       }
                        }
                        got_res1=0;
                        if(graph.isDag()!=true)
              			System.out.println("The 2nd current graph: " + graph);                    
                        System.out.println("1 Work done");
                        rResource[0].increaseRes();rResource[1].increaseRes();
                        graph.remove(0,2);graph.remove(1,2);
                    counter++;
                        }
                }
                catch(Exception ex){
                   System.out.println("hairan kadiya"); //ex.printStackTrace();
                }
                }
                else{
                    System.out.println("Deadlock");
                }
            }
        }
    private class jTh extends Thread{ 
        public void run(){
            //System.out.println(System.currentTimeMillis());
            
            if(System.currentTimeMillis()<endTimeMillis)
                {try{
                   while(counter<3 && System.currentTimeMillis()<endTimeMillis)
                    {
                        got_res2=0;
                        while(got_res2!=2 && System.currentTimeMillis()<endTimeMillis)
                        {
                            if(rResource[0].isAvailable()==true)
                            {
                                rResource[0].decreaseRes();
                                got_res2++;
                                graph.remove(3,0);
                                if(graph.isEdge(0,3)!=true)
                                graph.add(0,3);
                            }
                            else{
                            	if(graph.isEdge(3,0)!=true)
                            	graph.add(3,0);
                            }
                            if(rResource[1].isAvailable()==true)
                            {
                                rResource[1].decreaseRes();
                                got_res2++;     
                                graph.remove(3,1); 
                                if(graph.isEdge(1,3)!=true)          
                                graph.add(1,3);
                            }
                            else{
                            	if(graph.isEdge(3,1)!=true)
                            	graph.add(3,1);
                            }
                       /* if(graph.isDag()!=true)
              			{

                }        */  if(flag==1){ //                          I.suspend();
                            Thread.sleep(500);
                            //Thread solve=new solve();
                           // solve.start();
                           //try{solve.join();}catch(Exception ex){}
                           //I.resume();
                           //resume();
                           // I.join();
                           // J.join();
                           //System.out.println("The output graph: " + graph);
                            }                   
                        }
                        if(graph.isDag()!=true)
                      	System.out.println("The 4th current graph: " + graph);            
                        System.out.println("2 Work done");
                        got_res2=0;
                        rResource[0].increaseRes();rResource[1].increaseRes();
                        graph.remove(0,3);graph.remove(1,3);
                    counter++;
                    }
                }
                catch(Exception ex){
                   System.out.println("hairan kadiya"); //ex.printStackTrace();
                }
            }
                else{
                    System.out.println("Deadlock");
                }
            }
        }
    public cSection(){
        System.out.println("Starting Processes");
        Thread I= new iTh();
        Thread J = new jTh();
        //Thread S = new sTh();
        I.start(); //start process i
        J.start();//start process j
        try
        {
        I.join();
        J.join();
    	}
    	catch(Exception e)
    	{
    	}
          System.out.println("The current graph: " + graph);
    	}
        
        public static void main(String[] args){
        cSection cSec = new cSection();    
  }
}
