package baseball;

public class TeamInfo
{
	private int year;
	private String teamName;
	private int attendance;
	private int teamSalary;
	private int wins;
	private int losses;

	public TeamInfo() {}
	public TeamInfo(int year, String teamName)
	{
		this.year = year;
		this.teamName = teamName;
	}
	public TeamInfo(int year, String teamName, int attendance, int teamSalary, int wins, int losses)
	{
		this.year = year;
		this.teamName = teamName;
		this.attendance = attendance;
		this.teamSalary = teamSalary;
		this.wins = wins;
		this.losses = losses;
	}
	
	public int getYear()
	{
		return year;
	}
	public void setYear(int year)
	{
		this.year = year;
	}
	public String getTeamName()
	{
		return teamName;
	}
	public void setTeamName(String teamName)
	{
		this.teamName = teamName;
	}
	public int getAttendance()
	{
		return attendance;
	}
	public void setAttendance(int attendance)
	{
		this.attendance = attendance;
	}
	public int getTeamSalary()
	{
		return teamSalary;
	}
	public void setTeamSalary(int teamSalary)
	{
		this.teamSalary = teamSalary;
	}
	public int getWins()
	{
		return wins;
	}
	public void setWins(int wins)
	{
		this.wins = wins;
	}
	public int getLosses()
	{
		return losses;
	}
	public void setLosses(int losses)
	{
		this.losses = losses;
	}
	@Override
	public String toString()
	{
		return "TeamInfo [year=" + year + ", teamName=" + teamName + ", attendance=" + attendance + ", teamSalary="
				+ teamSalary + ", wins=" + wins + ", losses=" + losses + "]";
	}
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((teamName == null) ? 0 : teamName.hashCode());
		result = prime * result + year;
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TeamInfo other = (TeamInfo) obj;
		if (teamName == null)
		{
			if (other.teamName != null)
				return false;
		} else if (!teamName.equals(other.teamName))
			return false;
		if (year != other.year)
			return false;
		return true;
	}
}
