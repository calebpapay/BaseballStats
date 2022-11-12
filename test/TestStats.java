package baseball.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gradescope.jh61b.grader.GradedTest;

import baseball.StatsAnalyzer;
import baseball.TeamInfo;
import ledger.LoggedTest;

public class TestStats extends LoggedTest
{
	private StatsAnalyzer uut;
	
	@Before
	public void setup()
	{
		try
		{
			uut = new StatsAnalyzer("TeamsFranchises.csv", "Teams.csv", "Salaries.csv");
		} catch (IOException e)
		{
			e.printStackTrace();
			fail("Threw exception: " + e);
		}
	}
	
	@Test		
    @GradedTest(name="Test getTeamInfo()", max_score=5)
    public void testGet() 
    {
		TeamInfo expected = new TeamInfo(1998, "Chicago Cubs", 2623194, 50838000, 90, 73);
    	TeamInfo actual = uut.getTeamInfo("CHN", 1998);
    	assertEquals(expected, actual);
    	assertEquals(expected.getTeamSalary(), actual.getTeamSalary());
    	assertEquals(expected.getWins(), actual.getWins());
    	assertEquals(expected.getLosses(), actual.getLosses());
    	assertEquals(expected.getAttendance(), actual.getAttendance());
    	assertNull("Made up team, should have returned null", uut.getTeamInfo("TLT", 2022));
    }

	@Test		
    @GradedTest(name="Test getAverageCostPerWin()", max_score=5)
    public void testAverage() 
    {
		assertEquals(751064, uut.getAverageCostPerWin());
    }

	@Test		
    @GradedTest(name="Test costPerWinRankings()", max_score=5)
    public void testCost() 
    {
		List<TeamInfo> actual = uut.costPerWinRankings(1998);
		assertEquals(30, actual.size());
		assertEquals("The first team did not match the expected", new TeamInfo(1998, "Washington Nationals"), actual.get(0));
		assertEquals("The last team did not match the expected", new TeamInfo(1998, "Baltimore Orioles"), actual.get(29));
		double last = 0;
		for (TeamInfo team : actual)
		{
			assertTrue("Teams not sorted properly", team.getTeamSalary()/team.getWins() >= last);
		}
    }

	@Test		
    @GradedTest(name="Test getBestCostPerWinAllTime() and getBestCostPerWinTotalAllTime()", max_score=5)
    public void testBest() 
    {
		assertEquals("Best all time is not correct", new TeamInfo(1987, "Texas Rangers"), uut.getBestCostPerWinAllTime());
		Map<Integer, TeamInfo> actual = uut.getBestCostPerWinTotalAllTime();
		assertEquals(62, actual.size());
		assertEquals(new TeamInfo(2003, "Detroit Tigers"), actual.get(43));
		assertEquals(new TeamInfo(1988, "New York Mets"), actual.get(100));
    }

	@Test		
    @GradedTest(name="Test getWorstCostPerWinAllTime() and getWorstCostPerWinTotalAllTime()", max_score=10)
    public void testWorst() 
    {
		assertEquals("Worst all time is not correct", new TeamInfo(2013, "New York Yankees"), uut.getWorstCostPerWinAllTime());
		Map<Integer, TeamInfo> actual = uut.getWorstCostPerWinTotalAllTime();
		assertEquals(62, actual.size());
		assertEquals(new TeamInfo(2003, "Detroit Tigers"), actual.get(43));
		assertEquals(new TeamInfo(2015, "St. Louis Cardinals"), actual.get(100));
    }

	
	@Test		
    @GradedTest(name="Test getBestSpentPerAttendeeAllTime() and getWorstSpentPerAttendeeAllTime()", max_score=10)
    public void testAttendance() 
    {
		assertEquals("Best all time is not correct", new TeamInfo(1987, "Texas Rangers"), uut.getBestSpentPerAttendeeAllTime());
		assertEquals("Worst all time is not correct", new TeamInfo(2016, "Detroit Tigers"), uut.getWorstSpentPerAttendeeAllTime());
    }
	
	@Test
    @GradedTest(name="See the stats...", max_score=0)
	public void testPrintFYI()
	{
		System.out.println("Wins..........  ");
		TeamInfo best = uut.getBestCostPerWinAllTime();
		System.out.println("Best...  $" + best.getTeamSalary()/best.getWins() + " " +  best);
		TeamInfo worst = uut.getWorstCostPerWinAllTime();
		System.out.println("Worst... $" + worst.getTeamSalary()/worst.getWins() + " " + worst);
		System.out.println("Average: $" + uut.getAverageCostPerWin());

	
		System.out.println("Attendance..........  ");
		best = uut.getBestSpentPerAttendeeAllTime();
		double b = 1.0 * best.getTeamSalary()/best.getAttendance();
		System.out.println(String.format("Best...  $%.2f ", b) +  best);
		worst = uut.getWorstSpentPerAttendeeAllTime();
		System.out.println("Worst... $" + worst.getTeamSalary()/worst.getAttendance() + " " + worst);
		System.out.println("Average: $" + uut.getAverageSpentPerAttendee());
	}
	
	private static final String CODE_FILE= "src/baseball/StatsAnalyzer";
	@BeforeClass
	public static void grabCode()
	{
		LoggedTest.grabCode(CODE_FILE);
	}
}
