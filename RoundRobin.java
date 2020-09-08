import java.util.*;

public class RoundRobin{
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		int numProcess;
		do{
			System.out.print("Please enter number of process => ");
			numProcess = scan.nextInt();
		}while(numProcess < 3 || numProcess > 10);
		
		int[] arrivalTime = new int[numProcess];
		int[] burstTime = new int[numProcess];
		int[] p_id = new int [numProcess];
		int[] bursttime_array = new int [numProcess];
		int[] arrivaltime_array = new int [numProcess];
		int tq;
		
		// input details
		System.out.println("Enter details in form of burstTime | arrivalTime | priority ( eg 7 8 9 )");
		for (int i = 0; i < numProcess; i++){
			System.out.print("Please enter P" + i + " details => ");
			burstTime[i] = scan.nextInt();
			arrivalTime[i] = scan.nextInt();
			bursttime_array[i] = burstTime[i];
			arrivaltime_array[i] = arrivalTime[i];
			p_id[i] = i;
		}
		System.out.print("Enter time quantum = ");
		tq = scan.nextInt();
		System.out.println();
		
		// create input table
		System.out.println("===================================================");
		System.out.println("     		INPUT TABLE     			   ");
		System.out.println("===================================================");
		System.out.println();	
		System.out.println("---------------------------------------");
		System.out.println("| Process | Burst Time | Arrival Time  |");
		System.out.println("---------------------------------------");
		for (int i = 0; i < numProcess; i++){
			System.out.format("| P" + "%s" + "      | " + "%2d"  + "         |" + "%2d" + "            | " , p_id[i], burstTime[i], arrivalTime[i]);
			System.out.print("\n");
			System.out.println("---------------------------------------");
		}
		
		//Round Robin 
		boolean[] isReady = new boolean[p_id.length];
		for (int i = 0; i < arrivalTime.length - 1; i++) {
			for (int j = 0; j < arrivalTime.length - 1 - i; j++) {
				if (arrivalTime[j] > arrivalTime[j + 1]) {
					int temp1 = arrivalTime[j];
					arrivalTime[j] = arrivalTime[j + 1];
					arrivalTime[j + 1] = temp1;

					int temp2 = burstTime[j];
					burstTime[j] = burstTime[j + 1];
					burstTime[j + 1] = temp2;

					int temp3 = p_id[j];
					p_id[j] = p_id[j + 1];
					p_id[j + 1] = temp3;

				}

			}

		}
		
		int limit = 0;
		int totalBurstTime = 0;
		//store the time 
		ArrayList<Integer> chart = new ArrayList<Integer>();
		//store the time (adding up)
		ArrayList<Integer> addChart = new ArrayList<Integer>();
		//track the p_id
		ArrayList<Integer> track = new ArrayList<Integer>();
		
		LinkedList<Integer> ready = new LinkedList<Integer>();
		
		//here is the place that contain error/bug 
		int temp = 0;
		
		if(arrivalTime[temp] <= limit){
			if(burstTime[temp] > tq){
				burstTime[temp] = burstTime[temp] - tq;
				chart.add(tq);
				track.add(temp);
				totalBurstTime += tq;
				addChart.add(totalBurstTime);
				limit = limit + tq;
			} 
			else{
				chart.add(burstTime[temp]);
				track.add(temp);
				totalBurstTime += burstTime[temp];
				addChart.add(totalBurstTime);
				limit = limit + burstTime[temp];
				burstTime[temp] = 0;
			}
		}
		
		for (int i = 1; i < p_id.length; i++){
			if (arrivalTime[i] <= limit && isReady[p_id[i]] == false){
				ready.addLast(p_id[i]);
				isReady[p_id[i]] = true;
			}

		}
		
		while(!ready.isEmpty()){
			if(burstTime[temp] != 0){
				ready.addLast(p_id[temp]);

			}
			int temp2 = ready.removeFirst();
			for(int i = 0; i < p_id.length; i++){
				if(p_id[i] == temp2){
					temp = i;

				}
			}
			if(arrivalTime[temp] <= limit){
				if(burstTime[temp] > tq){
					burstTime[temp] = burstTime[temp] - tq;
					chart.add(tq);
					track.add(temp);
					totalBurstTime += tq;
					addChart.add(totalBurstTime);
					limit = limit + tq;

				} 
				else{
					chart.add(burstTime[temp]);
					track.add(temp);
					totalBurstTime += burstTime[temp];
					addChart.add(totalBurstTime);
					limit = limit + burstTime[temp];
					burstTime[temp] = 0;
				}
			}

			for (int i = 1; i < p_id.length; i++){
				if (arrivalTime[i] <= limit && isReady[p_id[i]] == false){
					ready.addLast(p_id[i]);
					isReady[p_id[i]] = true;
				}
			}

		}
		
		// [B] Calculate Total TurnAround & Waiting Time
		int[] ct = new int[p_id.length];
		int[] wt = new int[p_id.length];
		int[] tat = new int[p_id.length];
		for(int i = 0; i < ct.length; i++){
			int index = 0;
			for (int j = 0; j < track.size(); j++){
				if (i == track.get(j)){
					index = j;

				}
			}
			int s = 0;
			for (int k = 0; k <= index; k++){
				s = s + chart.get(k);
			}
			ct[i] = s;
		}
		
		for(int i = 0; i < tat.length; i++){
			int index = 0;
			for(int j = 0; j < p_id.length; j++){
				if (i == p_id[j]){
					index = j;
				}
			}
			tat[i] = ct[i] - arrivaltime_array[index];
		}
		
		for(int i = 0; i < wt.length; i++){
			int index = 0;
			for(int j = 0; j < p_id.length; j++){
				if (i == p_id[j]){
					index = j;
				}
			}
			wt[i] = tat[i] - bursttime_array[index];
		}
		
		double avgtat = 0;
		int totaltat = 0;
		for (int i = 0; i < tat.length; i++) {
			totaltat += tat[i];

		}
		avgtat = totaltat / p_id.length;
		double avgwt = 0;
		int totalwt = 0;
		for (int i = 0; i < wt.length; i++) {
			totalwt += wt[i];

		}
		avgwt = totalwt / p_id.length;
		
		// [C] Gannt chart
		System.out.println();
		System.out.println("===================================================");
		System.out.println("     		GANTT CHART	    			   ");
		System.out.println("===================================================");
		System.out.println();
		// [3] Round Robin
		System.out.print("\n");
		System.out.println("[3] Round-Robin\n");
		for(int i = 0; i < track.size(); i++){
			System.out.print("--------");
		}
		System.out.print("\n");
		for (int i = 0; i < track.size(); i++){
			System.out.format("|   P" + "%s" + "  ", track.get(i));
		}
		System.out.print("|");
		System.out.print("\n");
		for(int i = 0; i < track.size(); i++){
			System.out.print("--------");
		}
		System.out.print("\n");
		for (int i = 0; i < addChart.size(); i++){
			if (i == 0)
				System.out.print("0      ");
			System.out.format("%2d" + "      " , addChart.get(i)); 
		}	
		System.out.print("\n");
		
		
		// [3] Total/Average TA Time & Waiting Time Table
		System.out.println();
		System.out.println("--------------------------------------------");
		System.out.println("| Process | Turn Around Time | Waiting Time |");
		System.out.println("--------------------------------------------");
		for (int i = 0; i < tat.length; i++){
				System.out.format("| P" + "%s" + "      |" + "%2d" + "                |" + "%2d" + "            |", i , tat[i], wt[i]);
				System.out.print("\n");
				System.out.println("--------------------------------------------");
		}
		System.out.format("|Total    |" + "%2d" + "                |" + "%2d" + "            |", totaltat, totalwt);
		System.out.println("\n--------------------------------------------");
		System.out.format("|Average  |" + "%2f" + "         |" + "%2f" + "      |", avgtat, avgwt);
		System.out.println("\n--------------------------------------------");
	}
}
