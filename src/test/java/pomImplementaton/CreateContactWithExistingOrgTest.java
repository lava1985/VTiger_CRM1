package pomImplementaton;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import genericUtilities.DataType;
import genericUtilities.ExcelUtility;
import genericUtilities.IConstantPath;
import genericUtilities.JavaUtility;
import genericUtilities.PropertiesUtility;
import genericUtilities.TabNames;
import genericUtilities.WebDriverUtility;
import objectRepo.ContactInformationPage;
import objectRepo.ContactsPage;
import objectRepo.CreatingNewContactPage;
import objectRepo.HomePage;
import objectRepo.LoginPage;

public class CreateContactWithExistingOrgTest {

	public static void main(String[] args) {
		PropertiesUtility propertyUtil = new PropertiesUtility();
		ExcelUtility excel = new ExcelUtility();
		JavaUtility jutil = new JavaUtility();
		WebDriverUtility driverUtil = new WebDriverUtility();
		
		propertyUtil.propertiesInit(IConstantPath.PROPERTIES_FILE_PATH);
		excel.excelInit(IConstantPath.EXCIL_PATH);
		
		WebDriver driver = driverUtil.launchBrowser(propertyUtil.readFromProperties("browser"));
		driverUtil.maximizeBrowser();
		driverUtil.navigateToApp(propertyUtil.readFromProperties("url"));
		
		long time = (Long) jutil.convertStringToAnyDataType(propertyUtil.readFromProperties("timeouts"), 
																				DataType.LONG);
		driverUtil.waitTillElementFound(time);

		LoginPage login = new LoginPage(driver);
		HomePage home = new HomePage(driver);
		ContactsPage contact = new ContactsPage(driver);
		CreatingNewContactPage createContact = new CreatingNewContactPage(driver);
		ContactInformationPage contactInfo = new ContactInformationPage(driver);
		
		if (driver.getTitle().contains("vtiger CRM"))
			System.out.println("Login Page Displayed");
		else
			driverUtil.quitAllWindows();

		login.loginToVtiger(propertyUtil.readFromProperties("username"), propertyUtil.readFromProperties("password"));

		if (driver.getTitle().contains("Home"))
			System.out.println("Home Page is Displayed");
		else
			driverUtil.quitAllWindows();
		
		home.clickRequiredTab(driverUtil, TabNames.CONTACTS);
		if (driver.getTitle().contains("Contacts"))
			System.out.println("Contacts Page Displayed");
		else
			driverUtil.quitAllWindows();
		
		contact.clickCreateContactBTN();

		if (createContact.getPageHeader().equalsIgnoreCase("Creating new contact"))
			System.out.println("Creating New Contact Page is Displayed");
		else
			driverUtil.quitAllWindows();
		
		Map<String, String> map = excel.readFromExcel("ContactsTestData", "Create Contact With Organization");
		
		createContact.setContactLastName(map.get("Last Name"));
		createContact.selectExistingOrganization(driverUtil, map.get("Organization Name"));

		createContact.clickSaveBTN();

		if (contactInfo.getPageHeader().contains(map.get("Last Name")))
			System.out.println("Contact created successfully");
		else
			driverUtil.quitAllWindows();
		
		contactInfo.clickDeleteBTN();
		driverUtil.handleAlert("ok");
		
		if(driver.getTitle().contains("Contacts")) {
			System.out.println("Contacts Page is Displayed");
			excel.writeToExcel("ContactsTestData", "Create Contact With Organization", "Pass");
		}
		else {
			driverUtil.quitAllWindows();
			excel.writeToExcel("ContactsTestData", "Create Contact With Organization", "Fail");
		}
		
		excel.saveExcel(IConstantPath.EXCIL_PATH);

		home.signOutOfVtiger(driverUtil);
		excel.closeExcel();
		driverUtil.quitAllWindows();
	}

}
