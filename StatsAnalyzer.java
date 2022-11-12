package baseball;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StatsAnalyzer
{
	private Map<String, Map<Integer, TeamInfo>> teamData = new HashMap<>();
	private Map<Integer, TeamInfo> bestCostPerWin;
	private Map<Integer, TeamInfo> worstCostPerWin;
	private int winCount;
	private long salaryTotal;
	private long totalAttendance;
	private TeamInfo bestAttendance;
	private TeamInfo worstAttendance;
	private TeamInfo bestCost;
	private TeamInfo worstCost;
	
	/**
	 * Computes salary statistics from the files provided
	 * @param franchiseFile - the filename containing the Key for relating teams to franchises
	 * @param teamFile - the filename containing the information on individual teams each year
	 * @param salariesFile - the filename containing the salary data
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public StatsAnalyzer(String franchiseFile, String teamFile, String salariesFile) throws IOException
	{
//////
		Map<String, String> franchiseName = new HashMap<>();
		
		BufferedReader in = new BufferedReader(new FileReader(franchiseFile));
		String line = in.readLine();
		
		while (line != null)
		{
			String[] tokens = line.split(",");
			franchiseName.put(tokens[0],tokens[1]);
			line = in.readLine();
			
		}
//////////////
		in = new BufferedReader(new FileReader(teamFile));
		line = in.readLine();
		line = in.readLine(); //skip the header
		
		while (line != null)
		{
			String[] tokens = line.split(",");
			int year = Integer.parseInt(tokens[0]);
			if (year >= 1985 && year <= 2016)
			{
				TeamInfo team = new TeamInfo();
				if (tokens[42].length() > 0) 
				{
					team.setAttendance(Integer.parseInt(tokens[42]));
					totalAttendance += team.getAttendance();
				}
	
				team.setLosses(Integer.parseInt(tokens[9]));
				team.setTeamName(franchiseName.get(tokens[3])); //cross-walking?
				team.setTeamSalary(0);
				team.setWins(Integer.parseInt(tokens[8]));
				winCount += team.getWins();
				team.setYear(Integer.parseInt(tokens[0])); //string to integer
	
				String teamId = tokens[2];
				Map<Integer, TeamInfo> allTeams = teamData.get(teamId);
				
				if (allTeams == null) 
				{
					allTeams = new TreeMap<>();
					teamData.put(teamId,allTeams);
				}
				allTeams.put(team.getYear(), team);
					//common pattern in Maps
			}
			line = in.readLine();
		}
/////////////
		in = new BufferedReader(new FileReader(salariesFile));
		line = in.readLine();
		line = in.readLine(); //skip the header
			
		while (line != null)
		{
			String[] tokens = line.split(",");
			int salary = Integer.parseInt(tokens[4]);
			int year = Integer.parseInt(tokens[0]);
			
			String teamId = tokens[1];
			TeamInfo team = teamData.get(teamId).get(year);
			
			team.setTeamSalary(team.getTeamSalary()+salary);
			salaryTotal += salary;
			line = in.readLine();
		}
/////////////
	}

	/**
	 * Get the team information for a single team
	 * @param teamCode - the team code from the data
	 * @param year - the desired year
	 * @return the loaded team info for the specified team
	 */
	public TeamInfo getTeamInfo(String teamCode, int year)
	{
		if (teamData.get(teamCode) == null) {return null;}
		return teamData.get(teamCode).get(year);
	}
	
	/**
	 * Provides the average salary paid per win for all teams in the data set
	 * @return the average in dollars/win
	 */
	//private int winCount;
	//private long salaryTotal;
	public int getAverageCostPerWin()
	{
		//long result = salaryTotal/(long)winCount;
		//return (int)result;
	//BEHAVES THE SAME AS...
		return (int)(salaryTotal/winCount);
	}
	
	public List<TeamInfo> costPerWinRankings(int year)
	{
		List<TeamInfo> allTeamsForYear = new ArrayList<>();
		
		TeamInfo team;
		for (Map<Integer, TeamInfo> allTeams : teamData.values()) //teamData key is teamID, data is Map<Int, TeamInfo>
		{
			team = allTeams.get(year);
			
			if (team == null) {continue;}
			
			double costPerWin = team.getTeamSalary()/team.getWins();
			int insertLocation = allTeamsForYear.size();
			
			for (int i = 0; i < allTeamsForYear.size(); i++) 
			{
				TeamInfo otherTeam = allTeamsForYear.get(i);
				double otherCostPerWin = otherTeam.getTeamSalary()/otherTeam.getWins();
				if (costPerWin < otherCostPerWin)
				{
				//if costPerWin for this team is < the cost for the i'th team.
				//ranking least cost per win to greatest cost per win;
					insertLocation = i;
					break;
				}
			}
			
			allTeamsForYear.add(insertLocation, team);
		}
		
		return allTeamsForYear;
	}

	/**
	 * Get the team with the least amount of money spent per win across the entire dataset
	 * @return the team with the lowest salary/win
	 */
	public TeamInfo getBestCostPerWinAllTime()
	{
		if (bestCostPerWin == null)
		{
			buildCosts();
		}
		return bestCost;
	}

	/**
	 * Get the team with the most amount of money spent per win across the entire dataset
	 * @return the team with the highest salary/win
	 */
	public TeamInfo getWorstCostPerWinAllTime()
	{
		if (worstCostPerWin == null)
		{
			buildCosts();
		}
		return worstCost;
	}
	
	/**
	 * Get the team with the least amount of money spent per win across at each win total
	 * @return a map where the key is the number of wins and the value is the team with the best salary/win
	 */
	public Map<Integer, TeamInfo> getBestCostPerWinTotalAllTime()
	{
		if (bestCostPerWin == null)
		{
			buildCosts();
		}
		return bestCostPerWin;
	}

	/**
	 * Get the team with the most amount of money spent per win across at each win total
	 * @return a map where the key is the number of wins and the value is the team with the worst salary/win
	 */
	public Map<Integer, TeamInfo> getWorstCostPerWinTotalAllTime()
	{
		if (worstCostPerWin == null)
		{
			buildCosts();
		}
		return worstCostPerWin;
	}
	
	private void buildCosts()
	{
		bestCostPerWin = new TreeMap<>();
		worstCostPerWin = new TreeMap<>();
		
		for (Map<Integer, TeamInfo> allTeams : teamData.values()) //teamData key is teamID, data is Map<Int, TeamInfo>
		{
			for (TeamInfo team : allTeams.values()) 
			{
				double costPerWin = team.getTeamSalary()/team.getWins();
		////BUILD BEST
				if (bestCost == null || costPerWin < bestCost.getTeamSalary()/bestCost.getWins())
				{
					bestCost = team;
				}
				TeamInfo best = bestCostPerWin.get(team.getWins());
				if (best == null || costPerWin < best.getTeamSalary()/best.getWins()) 
				{
					bestCostPerWin.put(team.getWins(), team);
				}
		////BUILD WORST
				if (worstCost == null || costPerWin > worstCost.getTeamSalary()/worstCost.getWins())
				{
					worstCost = team;
				}
				TeamInfo worst = worstCostPerWin.get(team.getWins());
				if (worst == null || costPerWin > worst.getTeamSalary()/worst.getWins()) 
				{
				worstCostPerWin.put(team.getWins(), team);
				}
				
			}
		}
		return;
	}
	

	/**
	 * Provides the average salary paid per win for all teams in the dataset
	 * @return the average in dollars/win
	 */
	public int getAverageSpentPerAttendee()
	{
		return (int)(salaryTotal/totalAttendance);
	}
	
	/**
	 * Get the team with the least amount of money spent per attendee across the entire dataset
	 * @return the team with the lowest salary/attendee
	 */
	public TeamInfo getBestSpentPerAttendeeAllTime()
	{
		if (bestAttendance == null)
		{
			buildAtt();
		}
		return bestAttendance;
		
	}

	/**
	 * Get the team with the most amount of money spent per attendee across the entire dataset
	 * @return the team with the highest salary/attendee
	 */
	public TeamInfo getWorstSpentPerAttendeeAllTime()
	{
		if (worstAttendance == null)
		{
			buildAtt();
		}
		return worstAttendance;
	}
	
	private void buildAtt()
	{
		for (Map<Integer, TeamInfo> allTeams : teamData.values()) //teamData key is teamID, data is Map<Int, TeamInfo>
		{
			for (TeamInfo team : allTeams.values()) 
			{
				double costPerAtt = team.getTeamSalary()/team.getAttendance();
		////BUILD BEST
				if (bestAttendance == null || costPerAtt < bestAttendance.getTeamSalary()/bestAttendance.getAttendance())
				{
					bestAttendance = team;
				}
		////BUILD WORST
				if (worstAttendance == null || costPerAtt > worstAttendance.getTeamSalary()/worstAttendance.getAttendance())
				{
					worstAttendance = team;
				}
			}
		}
		return;
	}
	
}