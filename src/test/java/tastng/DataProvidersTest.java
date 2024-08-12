package tastng;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataProvidersTest {
@Test(dataProvider="data")
public void bookTicketTest(String from, String to)
{
	System.out.println("From" +from +"/tTo:"+to);
}
@DataProvider
public Object[][] data()
{
	Object[][] obj=new Object[3][2];
	obj[0][0]="Hyd";
	obj[0][1]="Channai";
	obj[1][0]="Hyd";
obj[1][1]="Delhi";
obj[2][0]="channai";
obj[2][1]="Hyd";
return obj;


}
}


