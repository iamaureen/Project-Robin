import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.lang.Integer;



public class DomParserDemo 
{
	public static boolean correctOrder(int numWires, String stringHolder[][], NodeList nListToCheck, String instructionHolder[]) {
		
		boolean orderMaintained = true;
		
		for (int i = 1; i < numWires + 1; i++) // Iterate through each of the instructions, not including the StartBlock
		{			
			String IdValue = stringHolder[0][i]; // Get the Id of the current instruction
			//System.out.println("IdValue :: "+IdValue);
									
			Element eToCheck = null; // Element object which will be initialized to the CMC of the current instruction
						
			for (int j = 0; j < nListToCheck.getLength(); j++) // Iterate through the CMCs to find a particular CMC by its "Id"
			{
				Node node = nListToCheck.item(j);				
				Element element = (Element) node;											
				if (element.getAttribute("Id").compareTo(IdValue) == 0)
				{
					eToCheck = element; 
				}
			}
			
			String blockInstruction = eToCheck.getAttribute("Target");			
			System.out.println("comparing " + blockInstruction + " vs " + instructionHolder[i - 1]);
			
			if (blockInstruction.compareTo(instructionHolder[i - 1]) != 0) {
				orderMaintained = false;
				break;
				
			}
		}
		return orderMaintained;
	}
	
	//method to check the arm movement 
	public static boolean mediumMotorDistanceRotationsDetection(Element e) // Returns true if moving downward, returns false if moving upward
	{
		boolean speedPos = true;
		boolean rotationsPos = true;
		
		NodeList cmt = e.getElementsByTagName("ConfigurableMethodTerminal"); 
		
		for (int i = 0; i < cmt.getLength(); i++)
		{
			Node cmtNode = cmt.item(i);
			Element cmtElement = (Element) cmtNode;			
			String configuredValueString = cmtElement.getAttribute("ConfiguredValue");			
			NodeList terminalList = cmtElement.getElementsByTagName("Terminal");			
			Node terminalNode = terminalList.item(0);
			Element terminalElement = (Element) terminalNode;			
			if (terminalElement.getAttribute("Id").compareTo("Speed") == 0)
			{
				double configuredValue = Double.parseDouble(configuredValueString);
				if (configuredValue < 0)
				{
					speedPos = false;
				}
			}
			
			if (terminalElement.getAttribute("Id").compareTo("Rotations") == 0)
			{
				double configuredValue = Double.parseDouble(configuredValueString);
				if (configuredValue < 0)
				{
					rotationsPos = false;
				}
			}			
		}
		
		if ((speedPos && rotationsPos) || (!speedPos && !rotationsPos))
		{
			return true; //true when going down
		}
		else
		{
			return false; //false when going up
		}
		
	}
	
	public static int mediumMotorDistanceRotationsRotationsRange(Element e) // Returns 0 for failure, 1 for satisfactory, 2 for sub-optimal, 3 for optimal
	{
		double configuredValue = 0;
		
		NodeList cmt = e.getElementsByTagName("ConfigurableMethodTerminal"); 
		
		for (int i = 0; i < cmt.getLength(); i++)
		{
			Node cmtNode = cmt.item(i);
			Element cmtElement = (Element) cmtNode;
			
			String configuredValueString = cmtElement.getAttribute("ConfiguredValue");
			
			NodeList terminalList = cmtElement.getElementsByTagName("Terminal");
			
			Node terminalNode = terminalList.item(0);
			Element terminalElement = (Element) terminalNode;
			
			if (terminalElement.getAttribute("Id").compareTo("Rotations") == 0)
			{
				configuredValue = Double.parseDouble(configuredValueString);
				break;
			}
		}
		
		if (configuredValue < .25 && configuredValue > .15 || configuredValue > -.25 && configuredValue < -.15)
		{
			return 3;
		}
		else if (configuredValue < .3 && configuredValue > .1 || configuredValue > -.3 && configuredValue < -.1)
		{
			return 2;
		}
		else if (configuredValue < .4 && configuredValue > 0 || configuredValue > -.4 && configuredValue < 0)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	public static boolean moveTankDistanceRotationsMovingForward(Element e) // Returns true if moving forward, returns false if moving backward
	{
		boolean speedLeftPos = true;
		boolean speedRightPos = true;
		boolean rotationsPos = true;
		
		NodeList cmt = e.getElementsByTagName("ConfigurableMethodTerminal"); 
		
		for (int i = 0; i < cmt.getLength(); i++)
		{
			Node cmtNode = cmt.item(i);
			Element cmtElement = (Element) cmtNode;
			
			String configuredValueString = cmtElement.getAttribute("ConfiguredValue");
			
			NodeList terminalList = cmtElement.getElementsByTagName("Terminal");
			
			Node terminalNode = terminalList.item(0);
			Element terminalElement = (Element) terminalNode;
			
			if (terminalElement.getAttribute("Id").compareTo("Speed\\ Left") == 0)
			{
				double configuredValue = Double.parseDouble(configuredValueString);
				if (configuredValue < 0)
				{
					speedLeftPos = false;
				}
			}
			
			if (terminalElement.getAttribute("Id").compareTo("Speed\\ Right") == 0)
			{
				double configuredValue = Double.parseDouble(configuredValueString);
				if (configuredValue < 0)
				{
					speedRightPos = false;
				}
			}
			
			if (terminalElement.getAttribute("Id").compareTo("Rotations") == 0)
			{
				double configuredValue = Double.parseDouble(configuredValueString);
				if (configuredValue < 0)
				{
					rotationsPos = false;
				}
			}
		}
		
		if ((speedLeftPos && speedRightPos && rotationsPos) || (!speedLeftPos && !speedRightPos && !rotationsPos))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean moveTankDistanceRotationsOnlyRightWheelMoving(Element e) // Returns true if only right wheel moving forward, returns false if otherwise
	{
		boolean speedLeftEqualsZero = false;
		boolean speedRightPos = false;
		
		NodeList cmt = e.getElementsByTagName("ConfigurableMethodTerminal"); 
		
		for (int i = 0; i < cmt.getLength(); i++)
		{
			Node cmtNode = cmt.item(i);			
			Element cmtElement = (Element) cmtNode;			
			String configuredValueString = cmtElement.getAttribute("ConfiguredValue");			
			NodeList terminalList = cmtElement.getElementsByTagName("Terminal");			
			Node terminalNode = terminalList.item(0);			
			Element terminalElement = (Element) terminalNode;
			
			if (terminalElement.getAttribute("Id").compareTo("Speed\\ Left") == 0)
			{
				double configuredValue = Double.parseDouble(configuredValueString);
				if (configuredValue == 0)
				{
					speedLeftEqualsZero = true;
				}
				//System.out.println("speedLeftEqualsZero::"+speedLeftEqualsZero+" configuredValue:: "+configuredValue);
			}
			
			if (terminalElement.getAttribute("Id").compareTo("Speed\\ Right") == 0)
			{
				double configuredValue = Double.parseDouble(configuredValueString);
				if (configuredValue > 0)
				{
					speedRightPos = true;
				}
				//System.out.println("speedRightPos::"+speedRightPos+" configuredValue:: "+configuredValue);
			}
		}
		
		boolean onlyRightWheelMoving = false;
		
		if (speedRightPos && speedLeftEqualsZero)
		{
			onlyRightWheelMoving = true;
		}
		
		return onlyRightWheelMoving;
	}
	
	public static int moveTankDistanceRotationsRange80cm(Element e) // Returns 0 for failure, 1 for satisfactory, 2 for sub-optimal, 3 for optimal
	{
		double configuredValue = 0;
		
		NodeList cmt = e.getElementsByTagName("ConfigurableMethodTerminal"); 
		
		for (int i = 0; i < cmt.getLength(); i++)
		{
			Node cmtNode = cmt.item(i);
			Element cmtElement = (Element) cmtNode;			
			String configuredValueString = cmtElement.getAttribute("ConfiguredValue");			
			NodeList terminalList = cmtElement.getElementsByTagName("Terminal");			
			Node terminalNode = terminalList.item(0);
			Element terminalElement = (Element) terminalNode;			
			if (terminalElement.getAttribute("Id").compareTo("Rotations") == 0)
			{
				configuredValue = Double.parseDouble(configuredValueString);
				break;
			}
		}
		
		if (configuredValue > 6.2 && configuredValue < 6.5 || configuredValue < -6.2 && configuredValue > -6.5 )
		{
			return 3;  //perfect, you got it right
		}
		else if (configuredValue > 5 && configuredValue < 6.2 || configuredValue < -5 && configuredValue > -6.2)
		{
			return 2;  //increase little rotation value so that it goes forward
		}
		else if (configuredValue > 3 && configuredValue < 5 || configuredValue < -3 && configuredValue > -5)
		{
			return 1; //goes half way because of configured value=3, increase your value
		}
		else
		{
			return 0;
		}
		
	}

	public static int moveTankDistanceRotationsRange40cm(Element e) // Returns 0 for failure, 1 for satisfactory, 2 for sub-optimal, 3 for optimal
	{
		double configuredValue = 0;
		
		NodeList cmt = e.getElementsByTagName("ConfigurableMethodTerminal"); 
		
		for (int i = 0; i < cmt.getLength(); i++)
		{
			Node cmtNode = cmt.item(i);
			Element cmtElement = (Element) cmtNode;
			
			String configuredValueString = cmtElement.getAttribute("ConfiguredValue");
			
			NodeList terminalList = cmtElement.getElementsByTagName("Terminal");
			
			Node terminalNode = terminalList.item(0);
			Element terminalElement = (Element) terminalNode;
			
			if (terminalElement.getAttribute("Id").compareTo("Rotations") == 0)
			{
				configuredValue = Double.parseDouble(configuredValueString);
				break;
			}
		}
		
		if (configuredValue > 1.9 && configuredValue < 2.1 || configuredValue < -1.9 && configuredValue > -2.1 )
		{
			return 3;
		}
		else if (configuredValue > 1.7 && configuredValue < 2.3 || configuredValue < -1.7 && configuredValue > -2.3)
		{
			return 2;
		}
		else if (configuredValue > 1 && configuredValue < 3 || configuredValue < -1 && configuredValue > -3)
		{
			return 1;
		}
		else
		{
			return 0;
		}
		
	}
	
	public static int moveTankDistanceRotationsRange90Turn(Element e) // Returns 0 for failure, 1 for satisfactory, 2 for sub-optimal, 3 for optimal
	{
		double configuredValue = 0;		
		NodeList cmt = e.getElementsByTagName("ConfigurableMethodTerminal"); 
		
		for (int i = 0; i < cmt.getLength(); i++)
		{
			Node cmtNode = cmt.item(i);			
			Element cmtElement = (Element) cmtNode;			
			String configuredValueString = cmtElement.getAttribute("ConfiguredValue");			
			NodeList terminalList = cmtElement.getElementsByTagName("Terminal");			
			Node terminalNode = terminalList.item(0);
			Element terminalElement = (Element) terminalNode;
			
			if (terminalElement.getAttribute("Id").compareTo("Rotations") == 0)
			{
				configuredValue = Double.parseDouble(configuredValueString);
				//System.out.println("Rotation Value:: "+configuredValue);
				break;
			}
		}
		
		if (configuredValue > .9 && configuredValue < 1.1)
		{
			return 3;
		}
		else if (configuredValue > .7 && configuredValue < 1.3)
		{
			return 2;
		}
		else if (configuredValue > 0 && configuredValue < 2)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

	public static int moveTankDistanceRotationsRange360Turn(Element e) // Returns 0 for failure, 1 for satisfactory, 2 for sub-optimal, 3 for optimal
	{
		double configuredValue = 0;
		
		NodeList cmt = e.getElementsByTagName("ConfigurableMethodTerminal"); 
		
		for (int i = 0; i < cmt.getLength(); i++)
		{
			Node cmtNode = cmt.item(i);
			
			Element cmtElement = (Element) cmtNode;
			
			String configuredValueString = cmtElement.getAttribute("ConfiguredValue");
			
			NodeList terminalList = cmtElement.getElementsByTagName("Terminal");
			
			Node terminalNode = terminalList.item(0);
			Element terminalElement = (Element) terminalNode;
			
			if (terminalElement.getAttribute("Id").compareTo("Rotations") == 0)
			{
				configuredValue = Double.parseDouble(configuredValueString);
				break;
			}
		}
		
		if (configuredValue > 3.9 && configuredValue < 4.1)
		{
			return 3;
		}
		else if (configuredValue > 3.7 && configuredValue < 4.3)
		{
			return 2;
		}
		else if (configuredValue > 3 && configuredValue < 5)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	public static void parse(String username, String filePath, int inputModuleNumber) // Parses input file according to the module production rules
	{
		int moduleNum = inputModuleNumber; // Read in the module number
		//System.out.println("Module Number::"+inputModuleNumber);
		try 
		{	
			
			File file = new File(filePath); // Initialize new File object from local path         
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance(); // Initialize new DocumentBuilderFactory object         
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); // Initialize new DocumentBuilder object using DocumentBuilderFactory object         
			Document doc = dBuilder.parse(file); // Initialize new Document object using the File object         
			doc.getDocumentElement().normalize(); // Normalize (?) the Document object         
			//System.out.println("Root element: " + doc.getDocumentElement().getNodeName()); // Get the name of the root element of the XML file        
			NodeList wList = doc.getElementsByTagName("Wire"); // Initialize new NodeList object of all "Wire" tags         
			//System.out.println("Number of wire tags: " + wList.getLength()); // Display number of "Wire" tags				
			int numWires = wList.getLength(); // Initialize numWires				
			int[][] holder = new int[2][numWires]; // Initialize 2-d array where each column will hold a SequenceOut/SequenceIn pair
				
			//get the sequence out numbers
			for (int i = 0; i < numWires; i++) // Iterate through the all "Wire" tags
			{
				Node wireNode = wList.item(i); // Instantiate Node object using the ith "Wire"				
				Element wireElement = (Element) wireNode; // Instantiate Element object using the Node object above 				
				String joints = wireElement.getAttribute("Joints"); // Store the SequenceOut/SequenceIn information in a string				
				//System.out.print("Joints:: "+joints + " "); // Print out the SequenceOut/SequenceIn string				
				// Print out the indices which immediately precede and immediately follow the SequenceOut number
				//System.out.print("Bookend indices are: " + joints.indexOf("n") + " " + joints.indexOf(":SequenceOut") + " "); 					
				int firstNumStartIndex = joints.indexOf("n"); // Store the firstNumStartIndex				
				int firstNumEndIndex = joints.indexOf(":SequenceOut"); // Store the firstNumEndIndex			
				String firstNumString = joints.substring(firstNumStartIndex + 1, firstNumEndIndex); // Store the SequenceOut number 					
				int firstNum = Integer.parseInt(firstNumString);  // Parse the SequenceOut number to an int				
				//System.out.println("So what we get is: " + firstNum); // Display the number of interest					
				holder[0][i] = firstNum; // Since these are the "firsts" in the pairs, we store in the first row of the 2-d array
					
			}
				
			//get the sequence in numbers
			for (int i = 0; i < numWires; i++) // Iterate through the all "Wire" tags
			{
				Node wire = wList.item(i); // Instantiate Node object using the ith "Wire"				
				Element wireElement = (Element) wire; // Instantiate Element object using the Node object above 				
				String joints = wireElement.getAttribute("Joints"); // Store the SequenceOut/SequenceIn information in a string				
				//System.out.print(joints + " "); // Print out the SequenceOut/SequenceIn string				
				//System.out.print("Bookend indices are: " + joints.lastIndexOf("(n") + " " + joints.indexOf(":SequenceIn") + " "); // Print out the indices which immediately precede (by 2) and immediately follow the SequenceIn number
				int secondNumStartIndex = joints.lastIndexOf("(n"); // Store the secondNumStartIndex				
				int secondNumEndIndex = joints.indexOf(":SequenceIn"); // Store the secondNumEndIndex					
				String secondNumString = joints.substring(secondNumStartIndex + 2, secondNumEndIndex); // Store the SequenceIn number					
				int secondNum = Integer.parseInt(secondNumString); // Parse the SequenceIn number to an int				
				//System.out.println("So what we get is: " + secondNum); // Display the number of interest
				
				holder[1][i] = secondNum; // Since these are the "seconds" in the pairs, we store in the second row of the 2-d array			
			}
				
			// Display the filled out 2-d array
//			for (int i = 0; i < 2; i++) 
//			{
//				for (int j = 0; j < numWires; j++)
//				{
//					System.out.print(holder[i][j] + " ");
//				}				
//				System.out.println();
//			}
           
			
			
			// Algorithm for finding the index (column) of the starting "Wire" pair
			int indexOfStart = 0;
			for (int i = 0; i < numWires; i++) 
			{
				int topNumber = holder[0][i];					
				boolean foundBelow = false;					
				for (int j = 0; j < numWires; j++)
				{
					if (topNumber == holder[1][j])
					{
						foundBelow = true;
					}
				}					
				if (!foundBelow)
				{
					indexOfStart = i;
					break;
				}
			}
				
			//System.out.println("indexOfStart: " + indexOfStart); // Display the index (column) of the starting "Wire" pair		
			
			// Swap in order to place the starting "Wire" pair in the first column
			int[][] startHolder = new int[2][1];			
			startHolder[0][0] = holder[0][indexOfStart]; //sequence out
			startHolder[1][0] = holder[1][indexOfStart]; //sequence in
			
			holder[0][indexOfStart] = holder[0][0];
			holder[1][indexOfStart] = holder[1][0];
				
			holder[0][0] = startHolder[0][0];
			holder[1][0] = startHolder[1][0];
			
//			System.out.println("Display the 2-d array after placing the starting Wire pair in the correct location");
//			for (int i = 0; i < 2; i++) // Display the 2-d array after placing the starting/ending "Wire" pair in the correct location
//			{
//				for (int j = 0; j < numWires; j++)
//				{
//					System.out.print(holder[i][j] + " ");
//				}
//				
//				System.out.println();
//			}
			
			// Algorithm for finding the index (column) of the ending "Wire" pair
			int indexOfEnd = 0;			
			for (int i = 0; i < numWires; i++) 
			{
				int bottomNumber = holder[1][i];					
				boolean foundAbove = false;					
				for (int j = 0; j < numWires; j++)
				{
					if (bottomNumber == holder[0][j])
					{
						foundAbove = true;
					}
				}					
				if (!foundAbove)
				{
					indexOfEnd = i;
					break;
				}
			}			
			//System.out.println("indexOfEnd: " + indexOfEnd); // Display the index (column) of the ending "Wire" pair
			
			// Swap in order to place the ending "Wire" pair in the last column
			
			int[][] endHolder = new int[2][1]; 
				
			endHolder[0][0] = holder[0][indexOfEnd];
			endHolder[1][0] = holder[1][indexOfEnd];
			
			holder[0][indexOfEnd] = holder[0][numWires - 1];
			holder[1][indexOfEnd] = holder[1][numWires - 1];
				
			holder[0][numWires - 1] = endHolder[0][0];
			holder[1][numWires - 1] = endHolder[1][0];
				
//			System.out.println("Display the 2-d array after placing the ending Wire pair in the correct location");
//			for (int i = 0; i < 2; i++) // Display the 2-d array after placing the starting/ending "Wire" pair in the correct location
//			{
//				for (int j = 0; j < numWires; j++)
//				{
//					System.out.print(holder[i][j] + " ");
//				}
//				
//				System.out.println();
//			}
			
			
			for (int i = 0; i < numWires - 3; i++) // Algorithm for placing the remaining pairs in the correct location
			{
				for (int j = i + 1; j < numWires - 1; j++)
				{
					if (holder[1][i] == holder[0][j]) // If the secondNum matches the firstNum of a pair, then we have pairs that should be contiguous
					{
						int[][] temp = new int[2][1]; // Swap to make pairs contiguous
							
						temp[0][0] = holder[0][j];
						temp[1][0] = holder[1][j];
							
						holder[0][j] = holder[0][i + 1];
						holder[1][j] = holder[1][i + 1];
							
						holder[0][i + 1] = temp[0][0];
						holder[1][i + 1] = temp[1][0];
					}
				}
			}
				
//			System.out.println("After sorting: "); // Display 2-d array after sorting algorithm
//			for (int i = 0; i < 2; i++)
//			{
//				for (int j = 0; j < numWires; j++)
//				{
//					System.out.print(holder[i][j] + " ");
//				}
//					
//				System.out.println();
//			}
				
			String stringHolder[][] = new String[1][numWires + 1]; // Instantiate new String array which will hold the sequence of CMCs by "Id" attribute
			
			for (int i = 0; i < numWires; i++)
			{
				stringHolder[0][i] = "n" + holder[0][i];
			}
			
			stringHolder[0][numWires] = "n" + holder[1][numWires - 1];
			
			// Display the sequence according to "Id" attribute
			//System.out.println("So sequence is: "); 
			
			for (int i = 0; i < numWires + 1; i++)
			{
				System.out.print(stringHolder[0][i] + " ");
			}
			
			System.out.println("\nModule Number :: "+moduleNum);
							
			// Need to add additional modules
			switch (moduleNum) 
			{
				case 1: 
				{	
					String wrongDirectionString = "You may want to consider which direction you're going in.";
					
					String[] rotationsString = new String[3];
					rotationsString[0] = "In order calculate the number of rotations, you'll need to use the number provided.";
					rotationsString[1] = "The number of rotations for the block is a little out of range. Try re-calculating.";
					rotationsString[2] = "The number of rotatinos for the block is only slightly off.";
					
					String toReturn = "";
					
					boolean errorFound = false;
					
					NodeList nListToCheck = doc.getElementsByTagName("ConfigurableMethodCall");
					
					for (int i = 1; i < numWires + 1; i++) // Iterate through each of the instructions, not including the StartBlock
					{
						String IdValue = stringHolder[0][i]; // Get the Id of the current instruction													
						Element eToCheck = null; // Element object which will be initialized to the CMC of the current instruction						
						nListToCheck = doc.getElementsByTagName("ConfigurableMethodCall"); // NodeList object of all CMCs						
						
						for (int j = 0; j < nListToCheck.getLength(); j++) // Iterate through the CMCs to find a particular CMC by their "Id"
						{
							Node node = nListToCheck.item(j);							
							Element element = (Element) node;
							
							if (element.getAttribute("Id").compareTo(IdValue) == 0)
							{
								eToCheck = element; 
							}
						}
						
						if (i == 1) // First non-trial instruction
						{
							boolean correctDirection = true;
							boolean correctRotations = true;

							if (moveTankDistanceRotationsMovingForward(eToCheck) == false) {
								
								correctDirection = false;
							}
							
							int rotationsScore = moveTankDistanceRotationsRange80cm(eToCheck);
							
							if (rotationsScore != 3) {
								
								correctRotations = false;
							}
							
							if (!correctDirection && !correctRotations) {
								
								toReturn += toReturn + wrongDirectionString + "Also, " + rotationsString[rotationsScore];
								
								errorFound = true;
							}
							else if (!correctDirection) {
								
								toReturn += toReturn + wrongDirectionString;
								
								errorFound = true;
							}
							else if (!correctRotations) {
							
								toReturn += toReturn +  rotationsString[rotationsScore];
								
								errorFound = true;
							}
							
							
						}

					
					} // End of iterating through each of the instructions
					

					
					if (!errorFound) {
						
						System.out.println("Great job! Your program was perfect!");
						//insert to db here
						
					}
					else {
						
						System.out.println(toReturn);
						//connect to mysql/rest service
						
//						Statement stmt = conn.createStatement();                          
//                      String sql = "INSERT INTO info VALUES ('ishrat', 1, 'That is incorrect')";
//                      stmt.executeUpdate(sql);
                        
                        RobinPHPService service = new RobinPHPService();
                        service.getResponse();
                        //hardcoded, TODO: input username and pass other variables
                        service.postResponse(1, username, "2", "I cannot do this");
						
					}
					
					break;
				} // End of case 0
				case 2:
				{
					// Start block is always correct because it is under a StartBlock tag instead of a ConfigurableMethodCallTag
					
					NodeList nListToCheck = doc.getElementsByTagName("ConfigurableMethodCall");
					
					String instructionHolder[] = {"MoveTankDistanceRotations\\.vix", "MediumMotorDistanceRotations\\.vix", "MediumMotorDistanceRotations\\.vix", "MoveTankDistanceRotations\\.vix", "StopBlock\\.vix"};
					
					boolean correctOrder = correctOrder(numWires, stringHolder, nListToCheck, instructionHolder);
					
					System.out.println("Correct Order: " + correctOrder +"\n");
										
					if (correctOrder)
					{
						boolean firstBlockCorrectSpeeds = false;
						int firstBlockCorrectRotations = 0;
						boolean secondBlockCorrectSpeeds = false;
						int secondBlockCorrectRotations = 0;
						
						for (int i = 1; i < numWires + 1; i++) // Iterate through each of the instructions, not including the StartBlock
						{
							String IdValue = stringHolder[0][i]; // Get the Id of the current instruction														
							Element eToCheck = null; // Element object which will be initialized to the CMC of the current instruction							
							nListToCheck = doc.getElementsByTagName("ConfigurableMethodCall"); // NodeList object of all CMCs
							
							for (int j = 0; j < nListToCheck.getLength(); j++) // Iterate through the CMCs to find a particular CMC by their "Id"
							{
								Node node = nListToCheck.item(j);								
								Element element = (Element) node;																
								if (element.getAttribute("Id").compareTo(IdValue) == 0)
								{
									eToCheck = element; 
									break;
								}
							}							
							if (i == 1) // First non-trial instruction
							{
								System.out.println("Checking whether it is going forward 80cm............");
								//check the value of power left and power right 
								if (moveTankDistanceRotationsMovingForward(eToCheck) == false) {
									
									System.out.println("Check the power left and power right values..maybe increase them a little bit");
								}
								
								//check for rotation range							
								int returnValue = moveTankDistanceRotationsRange80cm(eToCheck);
								
								if(returnValue==3)System.out.println("perfect, you got it right");
								else if(returnValue == 2)System.out.println("you are almost there, increase little rotation value so that it goes forward");
								else if(returnValue == 1)System.out.println("check on the rotation value, its only half way!");
								else System.out.println("Something Wrong!");
							}
							if (i == 2) //
							{
								System.out.println("Checking whether arm is going downward and its rotation  number............");
								//check whether arm is moving downwards, mediumMotorDistanceRotationsDetection returns true if arm is moving downwards, else false
								boolean retVal = mediumMotorDistanceRotationsDetection(eToCheck);
						
								if(retVal)System.out.println("Perfect Arm is moving downward");
								else System.out.println("Maybe you need to check something, arm not in the right direction");
								
								//check whether the arm moving is right rotations
								int returnValue = mediumMotorDistanceRotationsRotationsRange(eToCheck);
								
								if(returnValue==3)System.out.println("perfect, you got it right");
								else if(returnValue == 2)System.out.println("you are almost there, increase little rotation value so the arm goes down");
								else if(returnValue == 1)System.out.println("check on the rotation value, its only half way!");
								else System.out.println("Something Wrong!");
							}
							if(i == 3){
								
								System.out.println("Checking whether arm is going upward and its rotation  number............");
								//check whether arm is moving downwards, mediumMotorDistanceRotationsDetection returns true if arm is moving downwards, else false
								boolean retVal = mediumMotorDistanceRotationsDetection(eToCheck);
								
								if(!retVal)System.out.println("Perfect Arm is moving upward");
								else System.out.println("Maybe you need to check something, arm not in the right direction");
								
								//check whether the arm moving is right rotations
								int returnValue = mediumMotorDistanceRotationsRotationsRange(eToCheck);
								
								if(returnValue==3)System.out.println("perfect, you got it right");
								else if(returnValue == 2)System.out.println("you are almost there, increase little rotation value so the arm goes up");
								else if(returnValue == 1)System.out.println("check on the rotation value, its only half way!");
								else System.out.println("Something Wrong!");
								
							}
							if (i == 4){
								
								System.out.println("Checking whether it is going backward 80 cm............");
								//check the value of power left and power right 
								if (moveTankDistanceRotationsMovingForward(eToCheck) == false) {
									
									System.out.println("Check the power left and power right values..maybe increase them a little bit");
								}
								//check 80 cm backwards
								int returnValue = moveTankDistanceRotationsRange80cm(eToCheck);
								
								if(returnValue==3)System.out.println("perfect, you got it right");
								else if(returnValue == 2)System.out.println("you are almost there, increase little rotation value so that it goes backward");
								else if(returnValue == 1)System.out.println("check on the rotation value, its only half way!");
								else System.out.println("Something Wrong!");
							}
						}
					} // End of iterating through each of the instructions
					break;
				} // End Case 1
				case 3:
				{
					// Start block is always correct because it is under a StartBlock tag instead of a ConfigurableMethodCallTag
					boolean firstBlockCorrect = false;
					boolean secondBlockCorrect = false;
					boolean thirdBlockCorrect = false;
					boolean fourthBlockCorrect = false;
					
					for (int i = 1; i < numWires + 1; i++) // Iterate through each of the instructions, not including the StartBlock
					{
						String IdValue = stringHolder[0][i]; // Get the Id of the current instruction
												
						Element eToCheck = null; // Element object which will be initialized to the CMC of the current instruction
						
						NodeList nListToCheck = doc.getElementsByTagName("ConfigurableMethodCall"); // NodeList object of all CMCs
						
						for (int j = 0; j < nListToCheck.getLength(); j++) // Iterate through the CMCs to find a particular CMC by their "Id"
						{
							Node node = nListToCheck.item(j);
							
							Element element = (Element) node;
														
							if (element.getAttribute("Id").compareTo(IdValue) == 0)
							{
								eToCheck = element; 
							}
						}
						
						if (i == 1) // First non-trial instruction (StartBlock is trivial)
						{
							String blockInstruction = eToCheck.getAttribute("Target");
																
							if (blockInstruction.compareTo("MoveTankDistanceRotations\\.vix") == 0)
							{
								firstBlockCorrect = true;
							}
						}
						else if (i == 2) // Second non-trial instruction
						{
							String blockInstruction = eToCheck.getAttribute("Target");
														
							if (blockInstruction.compareTo("MoveTankDistanceRotations\\.vix") == 0)
							{
								secondBlockCorrect = true;
							}
						}
						else if (i == 3) // Third non-trial instruction
						{
							String blockInstruction = eToCheck.getAttribute("Target");
														
							if (blockInstruction.compareTo("MoveTankDistanceRotations\\.vix") == 0)
							{
								thirdBlockCorrect = true;
							}
						}
						else if (i == 4) // Third non-trial instruction
						{
							String blockInstruction = eToCheck.getAttribute("Target");
														
							if (blockInstruction.compareTo("StopBlock\\.vix") == 0)
							{
								fourthBlockCorrect = true;
							}
						}
					}
					
					boolean correctOrder = false;
					
					if (firstBlockCorrect && secondBlockCorrect && thirdBlockCorrect && fourthBlockCorrect) // If all blocks correct, we have correct order
					{
						correctOrder = true;
					}
					
					System.out.println("Correct Order: " + correctOrder);
					System.out.println();
					
					if (correctOrder)
					{
						boolean firstBlockCorrectDirection = false;
						int firstBlockCorrectRotations = 0;
						boolean secondBlockCorrectDirection = false;
						int secondBlockCorrectRotations = 0;
						boolean thirdBlockCorrectDirection = false;
						int thirdBlockCorrectRotations = 0;
						
						for (int i = 1; i < numWires + 1; i++) // Iterate through each of the instructions, not including the StartBlock
						{
							String IdValue = stringHolder[0][i]; // Get the Id of the current instruction
														
							Element eToCheck = null; // Element object which will be initialized to the CMC of the current instruction
							
							NodeList nListToCheck = doc.getElementsByTagName("ConfigurableMethodCall"); // NodeList object of all CMCs
							
							for (int j = 0; j < nListToCheck.getLength(); j++) // Iterate through the CMCs to find a particular CMC by their "Id"
							{
								Node node = nListToCheck.item(j);
								
								Element element = (Element) node;
																
								if (element.getAttribute("Id").compareTo(IdValue) == 0)
								{
									eToCheck = element; 
									break;
								}
							}
							
							if (i == 1) // First non-trial instruction
							{
								if (moveTankDistanceRotationsMovingForward(eToCheck) == true)
								{
									firstBlockCorrectDirection = true;
								}
								
								firstBlockCorrectRotations = moveTankDistanceRotationsRange40cm(eToCheck);
								
								System.out.println("Moving forward 40 cm");
								System.out.println("Correct direction: " + firstBlockCorrectDirection);
								System.out.println("Correct rotations (On a scale from 0-3): " + firstBlockCorrectRotations);
								System.out.println();
							}
							if (i == 2) // Second non-trial instruction
							{
								if (moveTankDistanceRotationsOnlyRightWheelMoving(eToCheck) == true)
								{
									secondBlockCorrectDirection = true;
								}
								
								secondBlockCorrectRotations = moveTankDistanceRotationsRange360Turn(eToCheck);
								
								System.out.println("Turn 360 degrees moving right wheel only");
								System.out.println("Correct direction: " + secondBlockCorrectDirection);
								System.out.println("Correct rotations (On a scale from 0-3): " + secondBlockCorrectRotations);
								System.out.println();
							}
							if (i == 3) // Third non-trial instruction
							{
								if (moveTankDistanceRotationsMovingForward(eToCheck) == true)
								{
									thirdBlockCorrectDirection = true;
								}
								
								thirdBlockCorrectRotations = moveTankDistanceRotationsRange40cm(eToCheck);
								
								System.out.println("Moving forward 40 cm");
								System.out.println("Correct direction: " + thirdBlockCorrectDirection);
								System.out.println("Correct rotations (On a scale from 0-3): " + thirdBlockCorrectRotations);
								System.out.println();
							}
						}
					} // End of iterating through each of the instructions
					break;
				}
				default:{
					System.out.println("This is the default");
				}
						
			} // End of switch cases
			
        } // End of try block
		catch(Exception e) 
		{
			e.printStackTrace();
		}
   } // End of parse method
} // End class