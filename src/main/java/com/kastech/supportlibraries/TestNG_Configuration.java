package com.kastech.supportlibraries;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

@Component
public class TestNG_Configuration {
	@Autowired
	Util utilObject;
    @Autowired
	ExecuteScripts testngDriver;
    @Autowired
	POI_ReadExcel poiObject;
    @Autowired
	ExecuteScripts driverObject;

	String mainresultsFolder="";
	String resultsFolder = "";
	boolean flag = false;

	@BeforeSuite
	public void CheckGridSetupValue(){

		if(!(utilObject.getValueFromConfigProperties("GridSetup").equalsIgnoreCase("Yes"))){
			JOptionPane.showMessageDialog(null,"Grid Setup is set to No, please execute Standalone Driver", "Configuration Error", JOptionPane.ERROR_MESSAGE);
		}else{
			try {

		//Process P =  Runtime.getRuntime().exec("cmd.exe /c start C:\\Users\\mamitra\\workspace\\Automation_Workspace\\Automation_Framework\\Jars\\Node_ChromeIEFirefox"+no +".bat");

				String homePath = new File (".").getCanonicalPath();

				String path = homePath+"\\TestCaseSelection.xlsx";
				FileInputStream file = new FileInputStream(new File(path));
				XSSFWorkbook workBook = new XSSFWorkbook(file);
				int numberOfSheets = workBook.getNumberOfSheets();
				workBook.close();

				int numberOfExecutedTCs = 0;

				for(int i=0;i<numberOfSheets;i++){
					List<String> whereClause1 = new ArrayList<String>();
					whereClause1.add("Execute::Yes");
					Map<String, List<String>> result1 = poiObject.fetchWithCondition(path, workBook.getSheetName(i), whereClause1);
					numberOfExecutedTCs = numberOfExecutedTCs+result1.get("Execute").size();
				}

				if(numberOfExecutedTCs!=0){
					mainresultsFolder=testngDriver.CreateDateFolder(homePath);
					resultsFolder = testngDriver.CreateExecutionFolder(mainresultsFolder);
					flag = true;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@AfterSuite
	public void CloseSummary(){
		//if flag value is true then the close summary step is executed
		if(flag){
			Report reportObject = new Report();
			reportObject.CloseSummary(ExecuteScripts.resultsPath);
		}
	}

	@Test
	  public void EarnedIncome() {



			//Process P =  Runtime.getRuntime().exec("cmd.exe /c start C:\\Users\\mamitra\\workspace\\Automation_Workspace\\Automation_Framework\\Jars\\Node_ChromeIEFirefox"+port_EI +".bat");

		if(flag){
			driverObject.ExecuteModule(Thread.currentThread().getStackTrace()[1].getMethodName(),resultsFolder);
		}



	}
	@Test
	  public void Household() {


	//	Process P =  Runtime.getRuntime().exec("cmd.exe /c start C:\\Users\\mamitra\\workspace\\Automation_Workspace\\Automation_Framework\\Jars\\Node_ChromeIEFirefox"+port_HI +".bat");

		if(flag){
			driverObject.ExecuteModule(Thread.currentThread().getStackTrace()[1].getMethodName(),resultsFolder);
		}

}

}
