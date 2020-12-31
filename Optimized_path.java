package Test;

import java.util.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/matrix")
public class Optimized_path {

    private static int INFINITY = 100000000;
    static int ar[];
    static int adjacencyMatrix[][];
    static String d="";
    static DriverRegister dr = new DriverRegister();
    static List<JsonObject> tempList = new ArrayList();
    static JsonObject specPlace = new JsonObject();
    static JsonObjects listOfPlace = new JsonObjects();
    static Register reg = new Register();
    static JsonObjects rec;
    static String[] arrayToSend = new String[100];
    static int val[]; 
    
    static int z = 0;
    static int time = 0;
    
    private static class Index {
        int currentVertex;
        Set<Integer> vertexSet;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Index index = (Index) o;

            if (currentVertex != index.currentVertex) return false;
            return !(vertexSet != null ? !vertexSet.equals(index.vertexSet) : index.vertexSet != null);
        }

        @Override
        public int hashCode() {
            int result = currentVertex;
            result = 31 * result + (vertexSet != null ? vertexSet.hashCode() : 0);
            return result;
        }

        private static Index createIndex(int vertex, Set<Integer> vertexSet) {
            Index i = new Index();
            i.currentVertex = vertex;
            i.vertexSet = vertexSet;
            return i;
        }
    }

    private static class SetSizeComparator implements Comparator<Set<Integer>>{
        @Override
        public int compare(Set<Integer> o1, Set<Integer> o2) {
            return o1.size() - o2.size();
        }
    }

    public static int minCost(int[][] distance) throws Exception {

        //stores intermediate values in map
    	System.out.println("inside min cost");
        Map<Index, Integer> minCostDP = new HashMap<>();
        Map<Index, Integer> parent = new HashMap<>();
        
        System.out.println("distance.lenght "+distance.length);
        List<Set<Integer>> allSets = generateCombination(distance.length - 1);
        System.out.println("inside all sets "+allSets);
        for(int i=0;i<allSets.size();i++)
        {
        	System.out.println("    *"+allSets.get(i));
        }
        
        for(Set<Integer> set : allSets) {
            for(int currentVertex = 1; currentVertex < distance.length; currentVertex++) {
                if(set.contains(currentVertex)) {
                    continue;
                }
                Index index = Index.createIndex(currentVertex, set);
                int minCost = INFINITY;
                int minPrevVertex = 0;
                //to avoid ConcurrentModificationException copy set into another set while iterating
                Set<Integer> copySet = new HashSet<>(set);
                for(int prevVertex : set) {
                    int cost = distance[prevVertex][currentVertex] + getCost(copySet, prevVertex, minCostDP);
                    if(cost < minCost) {
                        minCost = cost;
                        minPrevVertex = prevVertex;
                    }
                }
                //this happens for empty subset
                if(set.size() == 0) {
                    minCost = distance[0][currentVertex];
                }
                minCostDP.put(index, minCost);
                parent.put(index, minPrevVertex);
            }
        }

        Set<Integer> set = new HashSet<>();
        for(int i=1; i < distance.length; i++) {
            set.add(i);
        }
        int min = Integer.MAX_VALUE;
        int prevVertex = -1;
        //to avoid ConcurrentModificationException copy set into another set while iterating
        Set<Integer> copySet = new HashSet<>(set);
        for(int k : set) {
            int cost = distance[k][0] + getCost(copySet, k, minCostDP);
            if(cost < min) {
                min = cost;
                prevVertex = k;
            }
        }

        parent.put(Index.createIndex(0, set), prevVertex);
        printTour(parent, distance.length);
        return min;
    }

    private static void printTour(Map<Index, Integer> parent, int totalVertices) {
    	System.out.println("print tour");
    	Set<Integer> set = new HashSet<>();
        for(int i=0; i < totalVertices; i++) {
            set.add(i);
        }
        Integer start = 0;
        Deque<Integer> stack = new LinkedList<>();
        val = new int[10];
        int cnt = 0;
        while(true) {
            stack.push(start);
            System.out.println(start+" Start");
            
            val[cnt] = start;
            System.out.println("z "+cnt +val[cnt]);
            cnt++;
            
            set.remove(start);
            start = parent.get(Index.createIndex(start, set));
            if(start == null) {
                break;
            }
            z = cnt;
        }
        StringJoiner joiner = new StringJoiner("->");
        stack.forEach(v -> joiner.add(String.valueOf(v)));
        System.out.println("\nTSP tour");
        String join = joiner.toString();
        arrayToSend = join.split("->");
        for(int i=0;i<arrayToSend.length-1;i++)
        {
        	System.out.println(" "+arrayToSend[i]);
        }
        //System.out.println(" ********"+arrayToSend[0]);
 
        reg.cartArranged(arrayToSend);
        for(int i=0;i<arrayToSend.length - 2;i++)
        {
        	System.out.println("z "+z +" "+val[z]+" "+val[z-1]);
        	time = time + adjacencyMatrix[val[z]][val[z-1]];   
        	System.out.println("time"+time);
        	z--;
        }
        
        System.out.println("Final Time "+time);
        sendAdjtoReg();
    }

    private static int getCost(Set<Integer> set, int prevVertex, Map<Index, Integer> minCostDP) {
    	System.out.println("inside get costm");
    	set.remove(prevVertex);
        Index index = Index.createIndex(prevVertex, set);
        int cost = minCostDP.get(index);
        set.add(prevVertex);
        return cost;
    }

    private static List<Set<Integer>> generateCombination(int n) throws Exception {
        int input[] = new int[n];
        for(int i = 0; i < input.length; i++) {
            input[i] = i+1;
        }
        List<Set<Integer>> allSets = new ArrayList<>();
        int result[] = new int[input.length];
        generateCombination(input, 0, 0, allSets, result);
        Collections.sort(allSets, new SetSizeComparator());
        return allSets;
    }

    private static void generateCombination(int input[], int start, int pos, List<Set<Integer>> allSets, int result[]) throws Exception {
        if(pos == input.length) {
            return;
        }
        Set<Integer> set = createSet(result, pos);
        allSets.add(set);
        for(int i=start; i < input.length; i++) {
            result[pos] = input[i];
            generateCombination(input, i+1, pos+1, allSets, result);
        }
    }

    private static Set<Integer> createSet(int input[], int pos) throws Exception {
        if(pos == 0) {
            return new HashSet<>();
        }
        Set<Integer> set = new HashSet<>();
        for(int i = 0; i < pos; i++) {
            set.add(input[i]);
            reg.cartToOptimizedPath();
        }
        
        return set;
    }
    
    // --------------------------------         Accepting using object Mobile      -----------------------------------------
    
  @Path("/mobile/acceptMatrix")
	@POST	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public static int AcceptMobileMatrix(String matrix) throws Exception{
		System.out.println("inside matrix");
		System.out.println("inside matrix"+matrix);
		String[]  waste2 = matrix.split(":");
		String[]  array_w = waste2[1].split("\"");
		String[]  array = array_w[1].split(",");
	       for(int i=0;i<array.length;i++)
	       {
	           System.out.println(array[i]);
	       } 
		
	       int k = (int) Math.sqrt(array.length);
	       System.out.println("k: "+k);		
		adjacencyMatrix = new int[k][k];
		ar = new int[k*k+1];
		int index = dr.index;
		int x;
		System.out.println("size"+ar.length);
		System.out.println(k*k+" K*K");
		int count=0;
		
		for(int i=0;i< array.length;i++)
		{
		//System.out.println( matrix.getMatrix().size()+"   ");
		
			if(array[i].contains("mins"))
			{
				 d=array[i].replaceAll(" mins","");
			}
			else if(array[i].contains("min"))
			{
				 d=array[i].replaceAll(" min","");
			}
			
			int num = Integer.parseInt(d);
			System.out.println("mun: "+num);
			if(num == 1)
				num = 0;
			ar[i] = num;
			System.out.println("ar[i]: "+ar[i] + "i: "+i);
			x= i;
		}		
			for(int i=0;i<k;i++)
			{
				for(int j=0;j<k;j++)
				{
					adjacencyMatrix[i][j]=ar[count];
					count++;
				}
			}
			
			System.out.println("before arrangement should print");
			for(int o=0;o<k;o++)
			{
				for(int p=0;p<k;p++)
				{
					System.out.print(adjacencyMatrix[o][p]+"   ");
				}
				System.out.println();
			}
			
			int[] waste = new int[k];
			System.out.println("waste length" +waste.length);
			int[] waste1 = new int[k];
			for(int i=0;i<waste.length;i++)
			{
				waste[i] = adjacencyMatrix[i][0];
				waste1[i] = adjacencyMatrix[i][index];
				System.out.println(" waste[i] "+waste[i]+" waste1[i] "+waste1[i]);
			}
			for(int i=0;i<waste.length;i++)
			{
				adjacencyMatrix[i][index] = waste[i];
				adjacencyMatrix[i][0] = waste1[i];
				System.out.println("adjacencyMatrix[i][index]"+adjacencyMatrix[i][index]+" adjacencyMatrix[i][0] "+adjacencyMatrix[i][0]);
			}
			for(int i=0;i<waste.length;i++)
			{
				waste[i] = adjacencyMatrix[0][i];
				waste1[i] = adjacencyMatrix[index][i];
				System.out.println(" waste[i] "+waste[i]+" waste1[i] "+waste1[i]);
			}
			for(int i=0;i<waste.length;i++)
			{
				adjacencyMatrix[index][i] = waste[i];
				adjacencyMatrix[0][i] = waste1[i];
				System.out.println("adjacencyMatrix[1][i]"+adjacencyMatrix[index][i]+" adjacencyMatrix[0][i] "+adjacencyMatrix[0][i]);
			}
			
			
		System.out.println("I think this should print");
		for(int o=0;o<k;o++)
		{
			for(int p=0;p<k;p++)
			{
				System.out.print(adjacencyMatrix[o][p]+"   ");
			}
			System.out.println();
		}
		minCost(adjacencyMatrix);
		
		return time;
    }
    
    //--------------------------------         Accepting using string   website     -----------------------------------------
    
    @Path("/acceptMatrix")
	@POST	
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	public static int AcceptMatrix(String matrix) throws Exception{
		System.out.println("inside matrix"+matrix);
		int index = dr.index;
		System.out.println("the starting index should be: "+index);
		String[]  array = matrix.split(",");
	       for(int i=0;i<array.length;i++)
	       {
	           System.out.println(array[i]);
	       } 
		
	       int k = (int) Math.sqrt(array.length);
	       System.out.println("k: "+k);		
		adjacencyMatrix = new int[k][k];
		ar = new int[k*k+1];
		int x;
		System.out.println("size"+ar.length);
		System.out.println(k*k+" K*K");
		int count=0;
		
		for(int i=0;i< array.length;i++)
		{
		//System.out.println( matrix.getMatrix().size()+"   ");
		
		if(array[i].contains("mins"))
		{
			 d=array[i].replaceAll(" mins","");
		}
		else if(array[i].contains("min"))
		{
			 d=array[i].replaceAll(" min","");
		}
		
			//System.out.println(d+"Holla");
			int num = Integer.parseInt(d);
			System.out.println("mun: "+num);
			if(num == 1)
				num = 0;
			ar[i] = num;
			System.out.println("ar[i]: "+ar[i] + "i: "+i);
			x= i;
		}	
	//	ar[x+1] = dr.driver_Latitude;
		
			for(int i=0;i<k;i++)
			{
				for(int j=0;j<k;j++)
				{
					adjacencyMatrix[i][j]=ar[count];
					count++;
				}
					
			}
			System.out.println("before arrangement should print");
			for(int o=0;o<k;o++)
			{
				for(int p=0;p<k;p++)
				{
					System.out.print(adjacencyMatrix[o][p]+"   ");
				}
				System.out.println();
			}
			System.out.println("mat length: "+matrix.length());
			System.out.println("mat length: "+k);
			int[] waste = new int[k];
			int[] waste1 = new int[k];
			for(int i=0;i<waste.length;i++)
			{
				waste[i] = adjacencyMatrix[i][0];
				System.out.print(waste[i]+"i:    ");
				waste1[i] = adjacencyMatrix[i][1];
				System.out.print(waste1[i]+"i:    ");
			}
			for(int i=0;i<waste.length-1;i++)
			{
				adjacencyMatrix[i][1] = waste[i];
				adjacencyMatrix[i][0] = waste1[i];
			}
			for(int i=0;i<waste.length-1;i++)
			{
				waste[i] = adjacencyMatrix[0][i];
				waste1[i] = adjacencyMatrix[1][i];
			}
			for(int i=0;i<waste.length-1;i++)
			{
				adjacencyMatrix[1][i] = waste[i];
				adjacencyMatrix[0][i] = waste1[i];
			}
		System.out.println("I think this should print");
		for(int o=0;o<k;o++)
		{
			for(int p=0;p<k;p++)
			{
				System.out.print(adjacencyMatrix[o][p]+"   ");
			}
			System.out.println();
		}
		minCost(adjacencyMatrix);
		
		return time;
    }

	public static String[] sendAdjtoReg() {
		return arrayToSend;	
	}
}
