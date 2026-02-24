public class Date
{
	private int month;
	private int day;
	private int year;
	private static final int[] daysPerMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	public Date(int month, int day, int year)
	{
		if (month <= 0 || month > 12)
			throw new IllegalArgumentException("month must be 1-12.");
		if (day <= 0 || ( day > daysPerMonth[month] && !( month == 2 && day == 29) ))
		       throw new IllegalArgumentException("day out-of-range for specified month.");
		if (month == 2 && day == 29 && !(year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)))
			throw new IllegalArgumentException("day out-of-range for the specified month and year.");

		this.month = month;
		this.day = day;
		this.year = year;			
	}
	
	public int getMonth()
	{
		return month;
	}
	
	public int getDay()
	{
		return day;
	}
	
	public int getYear()
	{
		return year;
	}

	@Override
	public String toString()
	{
		return String.format("%d/%d/%d",month,day,year);
	}
}
