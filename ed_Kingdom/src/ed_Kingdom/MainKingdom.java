package ed_Kingdom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainKingdom {

	public static void main(String[] args) {
		
		String input = "C:\\Users\\ig.eddy.p.putra\\Desktop\\in.in";
		String line = "";
		List<String> lines = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(input));
			
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Convert to array
		String[] in = lines.toArray(new String[]{});
		
		List<String> result = new ArrayList<String>();
		result = findRegion(in);
		

	}
	
	public static List<String> findRegion(String[] in){
		List<String> result = new ArrayList<String>();
		
		int totalCase = Integer.parseInt(in[0]);
		int n = 0;
		int m, i = 0;
		String w ="";
		int lineNumber = 1;
		int start, end = 0;
		String x = "";
		
		while (i < totalCase) {
			
			//Line
			n = Integer.parseInt(in[lineNumber]);
			System.out.println("M "+ (i+1) +" = "+n);

			//column
			m = Integer.parseInt(in[lineNumber+1]);
			System.out.println("N "+ (i+1) +" = "+m);
			
			
			//index firstLine
			start = lineNumber+2;
			//index lastLine
			end = start+(n-1);
			System.out.println();
			
			//method for count region
			HashMap<String, Integer> countArmy = new HashMap<String, Integer>();
			countArmy = checkRegion(in, start, end);
			
			//print result
			List<String> keys = new ArrayList(countArmy.keySet());
			System.out.println("Case "+(i+1)+":");
			for (int j = 0; j < countArmy.size(); j++) {
				System.out.println(keys.get(j)+" "+countArmy.get(keys.get(j)));
			}
			
			result.add(x);

			lineNumber = lineNumber+n+2;
			i = i+1;
		}
		return result;
	}
	
	public static HashMap<String, Integer> checkRegion(String[] in, int start, int end) {
		
		String x = "";
		//Create GRID
		char[][] grid = new char[end-start+1][in[start].length()];
		int idx = 0;
		for (int i = start; i <= end; i++) {
			
			for (int j = 0; j < in[i].length(); j++) {
				grid[idx][j] =  in[i].charAt(j);
				System.out.print(grid[idx][j]);
			}
			System.out.println();
			idx = idx+1;
		}
		
		//Count army
		HashMap<String, Integer> countArmy = new HashMap<String, Integer>();
		countArmy = countArmies(grid);
		
		//Count contested
		HashMap<String, Integer> countContested = new HashMap<String, Integer>();
		countContested = countContest(grid);
		
		
		return countArmy;
	}
	
	//CountArmy
	public static HashMap<String, Integer> countArmies(char[][] grid) {
		
		HashMap<String, Integer> countArmy = new HashMap<String, Integer>();
		
		String emptyLand = ".";
		String mountain = "#";
		HashMap<String, String> checkDuplicateLine = new HashMap<String, String>();
		
		//DISTINC
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if ((!emptyLand.equals(""+grid[i][j])) && (!mountain.equals(""+grid[i][j]))) {
					countArmy.put(""+grid[i][j], 0);
					checkDuplicateLine.put(""+grid[i][j], ""+grid[i][j]);
				}
			}
		}
		
		//Merge Hmap
		List<String> keyArmy = new ArrayList(countArmy.keySet());
		String temp = "";
		for (int i = 0; i < grid.length; i++) {
			
			for (int j = 0; j < grid[i].length; j++) {
				for (int j2 = 0; j2 < keyArmy.size(); j2++) {
					if (keyArmy.get(j2).equals(""+grid[i][j]) && (checkDuplicateLine.containsKey(keyArmy.get(j2)))) {
						countArmy.merge(keyArmy.get(j2), 1, Integer::sum);
						checkDuplicateLine.remove(keyArmy.get(j2));
						temp = keyArmy.get(j2);
					}
				}
			}
			checkDuplicateLine.put(temp, temp);
		}
		
		return countArmy;

	}
	
	//CountContested
	public static HashMap<String, Integer> countContest(char[][] grid) {
		
		HashMap<String, Integer> countContested = new HashMap<String, Integer>();
		HashMap<Integer, Integer> countPlace = new HashMap<Integer, Integer>();
		
		String emptyLand = ".";
		String mountain = "#";
		int armyLine = 0;
		int armyColumn = 0;
		int count = 0;
		String curArmy = "";
		
		List<String> keywordRight = new ArrayList<String>();
		keywordRight = null;
				
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if ((!emptyLand.equals(""+grid[i][j])) && (!mountain.equals(""+grid[i][j]))) {
					armyLine = i;
					armyColumn = j;
					curArmy = ""+grid[i][j];
					boolean doneRight = false;
					// Find Right
					if (armyColumn <= grid[armyLine].length-2) {
						for (int j2 = armyColumn+1; j2 < grid[armyLine].length; j2++) {
							if (mountain.equals(""+grid[armyLine][j2])) {
								j2 = grid[armyLine].length-1;
							} else if ((doneRight == false) && (!curArmy.equals(""+grid[armyLine][j2])) &&(!emptyLand.equals(""+grid[armyLine][j2])) && (!mountain.equals(""+grid[armyLine][j2]))) {
								//countContested.merge(""+grid[armyLine][j2], 1, Integer::sum);
								countContested.merge(""+grid[i][j], 1, Integer::sum);
								countPlace.merge(armyLine, 1, Integer::sum);
								doneRight = true;
							}
						}
					}
					//end find right
					
					// Find Left
					boolean doneLeft = false;
					if (armyColumn >= 0 && doneRight == false) {
						
						for (int j2 = armyColumn-1; j2 >= 0; j2--) {
							if (mountain.equals(""+grid[armyLine][j2])) {
								j2 = 0;
							} else if ((doneLeft == false) && (!curArmy.equals(""+grid[armyLine][j2])) &&(!emptyLand.equals(""+grid[armyLine][j2])) && (!mountain.equals(""+grid[armyLine][j2]))) {
								countContested.merge(""+grid[i][j], 1, Integer::sum);
								countPlace.merge(armyLine, 1, Integer::sum);
								doneLeft = true;
							}
						}
					}
					//eof left
					
					//Find top
					boolean doneTop = false;
					if (armyLine >= 1 && doneRight == false && doneLeft == false) {
						if ((!curArmy.equals(""+grid[armyLine-1][armyColumn])) && (!emptyLand.equals(""+grid[armyLine-1][armyColumn])) && (!mountain.equals(""+grid[armyLine-1][armyColumn]))) {
							countContested.merge(""+grid[i][j], 1, Integer::sum);
							countPlace.merge(armyLine-1, 1, Integer::sum);
							doneTop = true;
						} else if (emptyLand.equals(""+grid[armyLine-1][armyColumn])) {
							doneTop = false;
							int curLine = armyLine-1;
							int curColumn = armyColumn;
							// To right
							for (int k = curColumn+1; k < grid[curLine].length; k++) {
								if ((doneTop==false) && (!curArmy.equals(""+grid[curLine][k])) && (!emptyLand.equals(""+grid[curLine][k])) && (!mountain.equals(""+grid[curLine][k]))) {
									countContested.merge(""+grid[i][j], 1, Integer::sum);
									countPlace.merge(curLine, 1, Integer::sum);
									doneTop = true;
								} else if (mountain.equals(""+grid[curLine][k])) {
									k = grid[curLine].length-1;
								}
							} //eof topright
							
							// To left
							for (int k = curColumn-1; k >= 0; k--) {
								if ((doneTop == false) && (!curArmy.equals(""+grid[curLine][k])) && (!emptyLand.equals(""+grid[curLine][k])) && (!mountain.equals(""+grid[curLine][k]))) {
									countContested.merge(""+grid[i][j], 1, Integer::sum);
									countPlace.merge(curLine, 1, Integer::sum);
									doneTop = true;
								} else if (mountain.equals(""+grid[curLine][k])) {
									k = 0;
								}
							} //eof topleft
						} 
					}
					// eof top
					
					//Find bottom
					boolean doneBottom = false;
					if (armyLine < grid.length-1 && doneRight == false && doneLeft == false && doneTop == false) {
						if ((!curArmy.equals(""+grid[armyLine+1][armyColumn])) && (!emptyLand.equals(""+grid[armyLine+1][armyColumn])) && (!mountain.equals(""+grid[armyLine+1][armyColumn]))) {
							countContested.merge(""+grid[i][j], 1, Integer::sum);
							countPlace.merge(armyLine+1, 1, Integer::sum);
							doneBottom = true;
						} else if (emptyLand.equals(""+grid[armyLine+1][armyColumn])) {
							doneBottom = false;
							int curLine = armyLine+1;
							int curColumn = armyColumn;
							// To right
							for (int k = curColumn+1; k < grid[curLine].length; k++) {
								if ((doneBottom==false) && (!curArmy.equals(""+grid[curLine][k])) && (!emptyLand.equals(""+grid[curLine][k])) && (!mountain.equals(""+grid[curLine][k]))) {
									countContested.merge(""+grid[i][j], 1, Integer::sum);
									countPlace.merge(curLine, 1, Integer::sum);
									doneBottom = true;
								} else if (mountain.equals(""+grid[curLine][k])) {
									k = grid[curLine].length-1;
								}
							} //eof btmright
							
							// To left
							for (int k = curColumn-1; k >= 0; k--) {
								if ((doneBottom == false) && (!curArmy.equals(""+grid[curLine][k])) && (!emptyLand.equals(""+grid[curLine][k])) && (!mountain.equals(""+grid[curLine][k]))) {
									countContested.merge(""+grid[i][j], 1, Integer::sum);
									countPlace.merge(curLine, 1, Integer::sum);
									doneBottom = true;
								} else if (mountain.equals(""+grid[curLine][k])) {
									k = 0;
								}
							} //eof btmleft
						} 
					}
					// eof btm

				}
			}
		}
		return countContested;

	}
	

}
