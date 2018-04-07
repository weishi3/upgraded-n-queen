package cs561hw1;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
/**
 * @author water
 *
 */
public class homework1 {
	
	
	public static ArrayList<Integer>twos =new ArrayList<Integer>();
	public static Map<Integer,ArrayList<Integer>> up0=new HashMap<Integer,ArrayList<Integer>>();
	public static Map<Integer,ArrayList<Integer>> down0=new HashMap<Integer,ArrayList<Integer>>();
	public static Map<Integer,ArrayList<Integer>> left0=new HashMap<Integer,ArrayList<Integer>>();//change to hash map?
	public static Map<Integer,ArrayList<Integer>> right0=new HashMap<Integer,ArrayList<Integer>>();

	
	
	public State deepCopyC(State old){
		State newone=this.new State(old);
		newone.zeros=new ArrayList<Integer>(old.zeros);
		newone.ones=new ArrayList<Integer>(old.ones);
		
		newone.validTrees=new ArrayList<Integer>(old.validTrees);
		newone.validTrees2=new ArrayList<Integer>(old.validTrees2);
		newone.coL0s=new HashMap<Integer,Integer>(old.coL0s);
		
		
		return newone;
	}
	
	public  void rOrD(HashMap<Integer,Integer>cm,int index){
		int value=cm.get(index);
		if (value==1)
			cm.remove(index);
		else
			cm.put(index,value-1);
	}
	
	
	public void modifyMap(int newLizard, State st){

		int n=st.n;
		st.numL--;
		st.ones.add(newLizard);
		
		
		rOrD(st.coL0s,st.zeros.remove(0)%n); //new add

			for (int i=newLizard+n;i<=n*n-1;i+=n){
				if (twos.contains(i) ){
					break;
				}
			
				if (st.zeros.remove((Object)i)){
					
					rOrD(st.coL0s,i%n);
				}
			}
	
			
			int rightEnd=newLizard/n*n+n-1;
			
			for (int i=newLizard+1;i<=rightEnd;i++){
				if (twos.contains(i) ){
	
					break;
				}
				
		
				if (st.zeros.remove((Object)i)){
					
					rOrD(st.coL0s,i%n);
				}
			}	


			boolean sideMark=false;
			for (int i=newLizard;;i=i+n-1){
				if (i/n==n-1 || i%n==0){
					sideMark=true;
				}
				if (i==newLizard) {
					if (sideMark) break;
					else continue;
				}
				
				if (twos.contains(i) ){
					break;
				}
				
				if (st.zeros.remove((Object)i)){
					
					rOrD(st.coL0s,i%n);
				}
				if (sideMark) break;
			}
			
			sideMark=false;
			for (int i=newLizard;;i=i+n+1){
				
				if (i/n==n-1 || i%n==n-1){
					sideMark=true;
				}
				if (i==newLizard) {
					if (sideMark) break;
					else continue;
				}
				if (twos.contains(i) ){
					break;
				}
				if (st.zeros.remove((Object)i)){
					
					rOrD(st.coL0s,i%n);
				}
				if (sideMark) break;
			}
		
		}
		

	
	public ArrayList<Integer> DFS(State st){
		
				if (st.numL==0)
			return st.ones;
		if (st.numL>0 && st.zeros.size()==0)
			return null;
		
		for (int i=0;i<st.validTrees.size();i++){
			int curTree=st.validTrees.get(i);
			ArrayList<Integer> rightList=right0.get(curTree);
			boolean rightOK=false;
			for (int j: rightList){
				if (st.zeros.contains(j)){
					rightOK=true;
					break;
				}
			}

			if (!rightOK){
				
				st.validTrees.remove(i);
				i--;
				continue;
			}
			ArrayList<Integer> leftList=left0.get(curTree);
			boolean leftOK=false;
			for (int j: leftList){
				if (st.zeros.contains(j)){
					leftOK=true;
					break;
				}
			}
			if (!leftOK){
				st.validTrees.remove(i);
				i--;
				continue;
			}
		}
		
		int restline= st.n-(st.zeros.get(0)/st.n);
		
		

		if (restline+st.validTrees.size()<st.numL){
			
			return null;
		}
		
		
		for (int i=0;i<st.validTrees2.size();i++){
			int curTree=st.validTrees2.get(i);
			ArrayList<Integer> downList=down0.get(curTree);
			boolean downOK=false;
			for (int j: downList){
				if (st.zeros.contains(j)){
					downOK=true;
					break;
				}
			}

			if (!downOK){
				
				st.validTrees2.remove(i);
				i--;
				continue;
			}
			ArrayList<Integer> upList=up0.get(curTree);
			boolean upOK=false;
			for (int j: upList){
				if (st.zeros.contains(j)){
					upOK=true;
					break;
				}
			}
			if (!upOK){
				st.validTrees2.remove(i);
				i--;
				continue;
			}
		}
		
		int restCol=st.coL0s.size();
		if (restCol+st.validTrees2.size()<st.numL){
			
			return null;
		}
		
		
		
		
		while (st.zeros.size() >=st.numL){
			int toPut=st.zeros.get(0);
			while(st.validTrees.size()>0 && st.validTrees.get(0)<toPut){
				st.validTrees.remove(0);
			}
			while(st.validTrees2.size()>0 && st.validTrees2.get(0)<toPut){
				st.validTrees2.remove(0);
			}
			if (st.numL>st.validTrees.size()+st.n-toPut/st.n) return null;
			State temp=this.deepCopyC(st);
			this.modifyMap(st.zeros.remove(0),temp);
			this.rOrD(st.coL0s,toPut%st.n);
			if (st.numL-1>st.validTrees2.size()+st.coL0s.size()) return null;
			ArrayList<Integer> tempResult=this.DFS(temp);
			if (tempResult!=null)
				return tempResult;
		}
		return null;
	}


	public static ArrayList<Integer> randomCombo(int numL,ArrayList<Integer> zeros){
		
		int zeroSize=zeros.size();
		ArrayList<Integer>ret=new ArrayList<Integer>();
		Random r = new Random();
		for (int i=0;i<numL;i++){
			int randomInt = r.nextInt(zeroSize);
			while(ret.contains(randomInt)){
				randomInt = r.nextInt(zeroSize);
			}
			ret.add(randomInt);
			
		}
		for (int i=0;i<numL;i++){
			ret.set(i, zeros.get(ret.get(i) ) );
		}
		
		Collections.sort(ret);
		return ret;
	}
	

	public static ArrayList<Integer> randomSuccessor(ArrayList<Integer> curR,ArrayList<Integer> zeroList,ArrayList<Integer>which){
		int zeroSize=zeroList.size();
		ArrayList<Integer> ret=new ArrayList<Integer>(curR);
		Random r = new Random();
		int thingToR = which.get(r.nextInt(which.size()));
		Random rr = new Random();
		int replaceTo=rr.nextInt(zeroSize);
		while(curR.contains(zeroList.get(replaceTo))){
			replaceTo=rr.nextInt(zeroSize);
		}
		ret.remove((Object)thingToR);
		ret.add(zeroList.get(replaceTo));
		Collections.sort(ret);
		return ret;
	}

	public static boolean nextWithProbability(int delta_E, double T){
		double toTo=0-delta_E/T;
		double probability=Math.exp(toTo);
		double r= Math.random();//>=0 <1
		if (r<=probability) return true;
		return false;
	}
	
	
	
	public static Object[] checkConflict(HashMap<Integer,Integer> state,ArrayList<Integer>combo,int n){
		ArrayList<Integer>which=new ArrayList<Integer>();
		int conflictCount=0;
		for (int i : combo){
			if (i==combo.get(combo.size()-1)) break;
			//right
			for(int j=i+1;j<=i/n*n+n-1;j++){
				if (state.get(j)==2) break;

				if (state.get(j)==1){
					if (!which.contains(i))
						which.add(i);
					if (!which.contains(j))
						which.add(j);
					
					conflictCount++;
				}
			}
		
			for(int j=i+n;j<=n*n-1;j+=n){
				if (state.get(j)==2) break;
				if (state.get(j)==1){
					if (!which.contains(i))
						which.add(i);
					if (!which.contains(j))
						which.add(j);
					conflictCount++;
				}
			}
			
			boolean sideMark=false;
			for (int j=i;;j=j+n-1){
				if (j/n==n-1 || j%n==0){
					sideMark=true;
				}
				if (j==i) {
					if (sideMark) break;
					else continue;
				}
				
				if (state.get(j)==2) break;
				if (state.get(j)==1){
					if (!which.contains(i))
						which.add(i);
					if (!which.contains(j))
						which.add(j);
					conflictCount++;
					//break;
				}
				if (sideMark) break;
			}
			
			sideMark=false;
			for (int j=i;;j=j+n+1){
				//reach side
				if (j/n==n-1 || j%n==n-1){
					sideMark=true;
				}
				if (j==i) {
					if (sideMark) break;
					else continue;
				}
				
				if (state.get(j)==2) break;
				if (state.get(j)==1){
					if (!which.contains(i))
						which.add(i);
					if (!which.contains(j))
						which.add(j);
					conflictCount++;
					//break;
				}
				if (sideMark) break;
			}
			
		}
		
		return new Object[] { conflictCount, which }; 
	}

	
	
	public ArrayList<Integer> BFS(State iniState){
		ArrayList<Integer>ret=null;
		Queue<State> qh = new LinkedList<State>();
		
		State currentState;
		qh.add(iniState);
		while(qh.size()>0){
			currentState=qh.remove();
		
			if (currentState.numL==1 &&currentState.zeros.size()>0){
				ret=currentState.ones;
				ret.add(currentState.zeros.get(0));
				return ret;
			}
			
			if (currentState.numL>=1 &&currentState.zeros.size()==0){
				continue;
			}
			
			for (int i=0;i<currentState.validTrees2.size();i++){
				int curTree=currentState.validTrees2.get(i);
				ArrayList<Integer> downList=down0.get(curTree);
				boolean downOK=false;
				for (int j: downList){
					if (currentState.zeros.contains(j)){
						downOK=true;
						break;
					}
				}
		
				if (!downOK){
					
					currentState.validTrees2.remove(i);
					i--;
					continue;
				}
				ArrayList<Integer> upList=up0.get(curTree);
				boolean upOK=false;
				for (int j: upList){
					if (currentState.zeros.contains(j)){
						upOK=true;
						break;
					}
				}
				if (!upOK){
					currentState.validTrees2.remove(i);
					i--;
					continue;
				}
			}
			
			int restCol= currentState.coL0s.size();
			if (restCol+currentState.validTrees2.size()<currentState.numL){
				
				continue;
			}
			
			for (int i=0;i<currentState.validTrees.size();i++){
				int curTree=currentState.validTrees.get(i);
				ArrayList<Integer> rightList=right0.get(curTree);
				boolean rightOK=false;
				for (int j: rightList){
					if (currentState.zeros.contains(j)){
						rightOK=true;
						break;
					}
				}

				if (!rightOK){
					
					currentState.validTrees.remove(i);
					i--;
					continue;
				}
				ArrayList<Integer> leftList=left0.get(curTree);
				boolean leftOK=false;
				for (int j: leftList){
					if (currentState.zeros.contains(j)){
						leftOK=true;
						break;
					}
				}
				if (!leftOK){
					currentState.validTrees.remove(i);
					i--;
					continue;
				}
			}
			
			int restline= currentState.n-(currentState.zeros.get(0)/currentState.n);
			if (restline+currentState.validTrees.size()<currentState.numL){
				
				continue;
			}
			
			
			while(currentState.zeros.size()>=currentState.numL){
				
				int toPut=currentState.zeros.get(0);
				while(currentState.validTrees.size()>0 && currentState.validTrees.get(0)<toPut){
					currentState.validTrees.remove(0);
				}
				while(currentState.validTrees2.size()>0 && currentState.validTrees2.get(0)<toPut){
					currentState.validTrees2.remove(0);
				}
				if (currentState.numL>currentState.validTrees.size()+currentState.n-toPut/currentState.n) break;
				
				State temp=this.deepCopyC(currentState);
				this.modifyMap(currentState.zeros.remove(0),temp);
				this.rOrD(currentState.coL0s,toPut%currentState.n);
				if (currentState.numL-1>currentState.validTrees2.size()+currentState.coL0s.size()) break;
				qh.add(temp);
			}
			
		}
		
		return ret;
	}
	public static void main(String[] args) throws IOException {  
		
		long startTime = System.currentTimeMillis();
	
		ArrayList<Integer> zeroList= new ArrayList<Integer>();
		ArrayList<Integer> oneList= new ArrayList<Integer>();
		ArrayList<Integer> validTreeList= new ArrayList<Integer>();
		ArrayList<Integer> validTreeList2= new ArrayList<Integer>();
		HashMap<Integer,Integer> initialMap=new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> colL0=new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> rowW0=new HashMap<Integer,Integer>();
		
		
		FileReader fr = new FileReader("./src/cs561hw1/input.txt");
		BufferedReader bf = new BufferedReader(fr);
		String method= bf.readLine();
		int n=Integer.parseInt(bf.readLine());
		int numL=Integer.parseInt(bf.readLine());
		
		
		
		readMapFromFile(zeroList, validTreeList, validTreeList2, initialMap,
				colL0,rowW0, bf, n);
		bf.close();
		fr.close();

		initializeDetectors(n, zeroList, validTreeList, validTreeList2);
		
		ArrayList<Integer> ans=null;
		homework1 hw=new homework1();
		State initialState=hw.new State(numL,n,zeroList,oneList,validTreeList,validTreeList2,colL0);
		//System.out.println(validTreeList);
		//System.out.println(validTreeList2);
		if (numL<=rowW0.size()+validTreeList.size() && numL<=colL0.size()+validTreeList2.size()){
			if (method.equals("DFS")){
				
				ans=hw.DFS(initialState);
				
			
			}else if (method.equals("BFS")){
				ans=hw.BFS(initialState);
				
			}else if (method.equals("SA")){
				//index list
				if(n==2 && numL==2)
					ans=null;
				else{
					System.out.println("here");
					ans = SA(zeroList, initialMap, n, numL, ans);
				}
			}
		}
			
			FileWriter fw = new FileWriter("./src/cs561hw1/output.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			
			if (ans==null){
				bw.write("FAIL");
				
			}else{
				int mapping[][]=new int[n][n];
				
				for (int i=0;i<n;i++){
					for (int j=0;j<n;j++){
						mapping[i][j]=0;
					}
				}
				for (int i:ans){
					mapping[i/n][i%n]=1;
				}
			
				
				for (int i:twos){
					mapping[i/n][i%n]=2;
				}
				
				bw.write("OK");
				
				for (int i=0;i<n;i++){
					bw.newLine();
					for (int j=0;j<n;j++){
						bw.write(Integer.toString(mapping[i][j]));
					}
				}
				
				}
			bw.close();
			fw.close();	
			
	
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime+"ms");
	}

	private static ArrayList<Integer> SA(ArrayList<Integer> zeroList,
			HashMap<Integer, Integer> initialMap, int n, int numL,
			ArrayList<Integer> ans) {
		ArrayList<Integer> curR=randomCombo(numL,zeroList);
		

		ArrayList<Integer> whichC=new ArrayList<Integer>();
		ArrayList<Integer> whichNC=new ArrayList<Integer>();
		ArrayList<Integer> nextR=null;
		

		
		HashMap<Integer, Integer> copyMap=new HashMap<Integer, Integer>(initialMap);
		for (int i:curR){
			copyMap.put(i, 1);
			
		}
		
		int curE=(int)checkConflict(copyMap,curR,n)[0];
		whichC=(ArrayList<Integer>) checkConflict(copyMap,curR,n)[1];
		
		int nextE=1000000; //any value
		int deltaE=nextE-curE; //any value
		Double T=100000.0;
		long SAS   = System.currentTimeMillis();
		
		int count=0;
		int countRepeatState=0;
		
		Double reheat=5.0;
		while(true){
			
			long thisTime=System.currentTimeMillis()-SAS;
			if (thisTime>=290000){
				break;
			}
			
			if (curE==0){
				ans=curR;
				break;
			}
			nextR=randomSuccessor(curR,zeroList,whichC);
			

			copyMap=new HashMap<Integer, Integer>(initialMap);
			for (int i:nextR){
				copyMap.put(i, 1);
				
			}
			

			nextE=(int) checkConflict(copyMap,nextR,n)[0];
			whichNC=(ArrayList<Integer>) checkConflict(copyMap,nextR,n)[1];
			deltaE=nextE-curE;
			if (deltaE<0){
				curR=nextR;
				curE=nextE;
				count=0;
				whichC=whichNC;
				
				countRepeatState=0;
			}else {
				if (curE<=3)
					count++;
				else count=0;
				
				if(nextWithProbability(deltaE,T)){
					if (nextE>curE) count=0;
					curR=nextR;
					curE=nextE;
					whichC=whichNC;
					
					countRepeatState=0;
				}else if (curE<=3)
					countRepeatState++;
			}
			//?
			T=T*0.99;
			if (count==250000 ){
				T=1.0;
				 whichC=curR;
			}
			if (count>250000 &&count<=250500) whichC=curR;
			

			if (count>=500000&&count<=501000){
				whichC=curR;
			}
				if (count==501000) count=0;
				if (count==500000) {
					T=reheat;
					reheat+=2.5;
				}

			if (countRepeatState==100000) {
		
				T=reheat;
				reheat+=2.5;
				whichC=curR;
				count=0;
				countRepeatState=0;
			}
				

			nextR=new ArrayList<Integer>();
		}
		return ans;
	}

	private static void readMapFromFile(ArrayList<Integer> zeroList,
			ArrayList<Integer> validTreeList,
			ArrayList<Integer> validTreeList2,
			HashMap<Integer, Integer> initialMap,
			HashMap<Integer, Integer> colL0,HashMap<Integer, Integer> rowW0, BufferedReader bf, int n)
			throws IOException {
		int tempIndex=0;
		for (int i=0;i<n;i++){
			for (int j=0;j<n;j++){
				if (bf.read()-48==2){
					twos.add(tempIndex);
					
					initialMap.put(tempIndex, 2);
					int leftright=tempIndex%n;
					if (leftright!=0 && leftright!=n-1) validTreeList.add(tempIndex);
					int updown=tempIndex/n;
					if (updown!=0 && updown!=n-1) validTreeList2.add(tempIndex);
					
				}else{
					zeroList.add(tempIndex);
					initialMap.put(tempIndex, 0);
					if (!colL0.containsKey(tempIndex%n))
						colL0.put(tempIndex%n, 1);
					else
						colL0.put(tempIndex%n,colL0.get(tempIndex%n)+1);
					
					if (!rowW0.containsKey(tempIndex/n))
						rowW0.put(tempIndex/n, 1);
					else
						rowW0.put(tempIndex/n,rowW0.get(tempIndex/n)+1);
					
				}
				
				tempIndex++;
			}

			bf.readLine();
		}
	}

	private static void initializeDetectors(int n, ArrayList<Integer> zeroList,
			ArrayList<Integer> validTreeList, ArrayList<Integer> validTreeList2) {
		boolean dontCheckLeft=false;
		for (int i=0;i<validTreeList.size();i++){
			
			boolean leftCan=false;
			int curTree=validTreeList.get(i);
			//-1+1 necessary
			if (!dontCheckLeft){
				for (int j=curTree-1;j>=curTree/n*n;j--){
					//valid left?
					if (twos.contains(j))
						continue;
					if (zeroList.contains(j)){
						leftCan=true;
						break;
					}
				}
				if (leftCan==false){
					validTreeList.remove(i);
					i--;
					continue;
				}
			}
			
			dontCheckLeft=false;
			boolean rightCan=false;
			for (int j=curTree+1;j<=curTree/n*n+n-1;j++){
				if (zeroList.contains(j)){
					rightCan=true;
					break;
				}
				
				if (twos.contains(j)){
					if (validTreeList.contains(j))
						dontCheckLeft=true;
				
					break;
				}
			}
			if (!rightCan){
				validTreeList.remove(i);
				i--;
				
			}
			
		}

		for (int i:validTreeList ){
			ArrayList<Integer> left=new ArrayList<Integer>();
			ArrayList<Integer> right=new ArrayList<Integer>();
			left0.put(i, left);
			right0.put(i, right);
			for (int j=i+1;j<=i/n*n+n-1;j++){
				if (twos.contains(j))
					break;
				right.add(j);
			}
			int tolerance=1;
			for (int j=i-1;j>=i/n*n;j--){
				if (twos.contains(j)){
					if (tolerance==1) continue;
					break;
				}
				left.add(j);
				tolerance=0;
			}
			
		}
	
		boolean dontCheckUp=false;
		for (int i=0;i<validTreeList2.size();i++){
			
			boolean upCan=false;
			int curTree=validTreeList2.get(i);
			//-1+1 necessary
			if (!dontCheckUp){
				for (int j=curTree-n;j>=0;j-=n){
					//valid left?
					if (twos.contains(j))
						continue;
					if (zeroList.contains(j)){
						upCan=true;
						break;
					}
				}
				if (upCan==false){
					validTreeList2.remove(i);
					i--;
					continue;
				}
			}
			
			dontCheckUp=false;
			boolean downCan=false;
			for (int j=curTree+n;j<=n*n-1;j+=n){
				if (zeroList.contains(j)){
					downCan=true;
					break;
				}
			
				if (twos.contains(j)){
					if (validTreeList2.contains(j))
						dontCheckUp=true;
				
					break;
				}
			}
			
			if (!downCan){
				validTreeList2.remove(i);
				i--;
				
			}
			
		}
	
		for (int i:validTreeList2 ){
			ArrayList<Integer> up=new ArrayList<Integer>();
			ArrayList<Integer> down=new ArrayList<Integer>();
			up0.put(i, up);
			down0.put(i, down);
			for (int j=i+n;j<=n*n-1;j+=n){
				if (twos.contains(j))
					break;
				down.add(j);
			}
			int tolerance=1;
			for (int j=i-n;j>=0;j-=n){
				if (twos.contains(j)){
					if (tolerance==1) continue;
					break;
				}
				up.add(j);
				tolerance=0;
			}
			
		}
	}
	public class State{
		public  ArrayList<Integer>zeros;
		public ArrayList<Integer>ones;
		
		public  ArrayList<Integer>validTrees;
		public  ArrayList<Integer>validTrees2;
		public int numL;
		public int n;
		public HashMap<Integer,Integer>coL0s=new HashMap<Integer,Integer>();
		
		public State(int numL,int n, ArrayList<Integer>zeroSet,ArrayList<Integer>oneSet,ArrayList<Integer>validTrees,ArrayList<Integer>validTrees2,HashMap<Integer,Integer>coL0s){
			this.numL=numL;
			this.n=n;
			this.zeros=zeroSet;
			this.ones=oneSet;
			
			this.validTrees=validTrees;
			this.validTrees2=validTrees2;
			this.coL0s=coL0s;
		}
		public State(State old){
			this.numL=old.numL;
			this.n=old.n;
			this.zeros=old.zeros;
			this.ones=old.ones;
		
			this.validTrees=old.validTrees;
			this.validTrees2=old.validTrees2;
			this.coL0s=old.coL0s;
			
			
		}
	}
}


