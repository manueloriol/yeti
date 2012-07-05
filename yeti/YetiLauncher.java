package yeti;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
//import java.awt.Toolkit;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.lang.*;
import java.util.ArrayList;
/**
 
 YETI - York Extensible Testing Infrastructure
 
 Copyright (c) 2009-2010, Manuel Oriol <manuel.oriol@gmail.com> - University of York
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 1. Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 3. All advertising materials mentioning features or use of this software
 must display the following acknowledgment:
 This product includes software developed by the University of York.
 4. Neither the name of the University of York nor the
 names of its contributors may be used to endorse or promote products
 derived from this software without specific prior written permission.
 
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER ''AS IS'' AND ANY
 EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
 **/ 




public class YetiLauncher extends JFrame {

	private JPanel contentPane;
	private JTextField txtItDisplayThe;
	String language = "-java";
	String time = "-time=5mn";
	String strategy = "-random";
	String gui = "-gui";
	String fileName = "-testModules=yeti.test.YetiTest"; 
	String logs = "-nologs";
	public String[] command;
	ArrayList<String> list = new ArrayList<String>();
	ArrayList<String> filesToCompile = new ArrayList<String>();
	String[] filesToCompileArray;
	JCheckBox GuiCheckBox;
	JCheckBox LogsCheckBox;
	JComboBox time1ComboBox;
	JComboBox time2ComboBox;
	String testFilePath = "-yetiPath=.";
	Thread t1 = new Thread(new Threado());
	String path = ".";
	private JTextField textField;	
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				//try {
					YetiLauncher frame = new YetiLauncher();
					
					//instructions to make the application run full screen.
					//Toolkit tk = Toolkit.getDefaultToolkit();
					//int xSize = ((int) tk.getScreenSize().getWidth());
					//int ySize = ((int) tk.getScreenSize().getHeight());
					// finish here. dont forget to import toolkit.
					//frame.setSize(xSize,ySize);
					frame.setVisible(true);
			//	} catch (Exception e) {
			//		e.printStackTrace();
			//	}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public YetiLauncher() {
		
		displayLabels();
		languageComboBoxMethod();
		strategyComboBoxMethod();
		timeComboBoxMethod();
		logsCheckBoxMethod();
		guiCheckBoxMethod();
		browseButtonMethod();
		runButtonMethod();
		
	}
	
	
	
	//@@@@@@@@@@@@@@@@@@@@@@@@ Method to Display All Labels @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//
	
	public void displayLabels(){
						
		setTitle("YETI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(700, 100, 1000, 617);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(169, 169, 169));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblYorkExtensibleTesting = new JLabel("York Extensible Testing Infrastructure");
		lblYorkExtensibleTesting.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		lblYorkExtensibleTesting.setBounds(111, 25, 389, 16);
		contentPane.add(lblYorkExtensibleTesting);
		
		JLabel lblLanguage = new JLabel("Language:");
		lblLanguage.setBounds(58, 94, 88, 16);
		contentPane.add(lblLanguage);
		
		JLabel lblStrategy = new JLabel("Strategy:");
		lblStrategy.setBounds(58, 122, 88, 16);
		contentPane.add(lblStrategy);
		
		JLabel lblDuration = new JLabel("Duration:");
		lblDuration.setBounds(58, 150, 88, 16);
		contentPane.add(lblDuration);
		
		JLabel lblGui = new JLabel("GUI:");
		lblGui.setBounds(58, 178, 88, 16);
		contentPane.add(lblGui);
		
		JLabel lblLogs = new JLabel("Logs:");
		lblLogs.setBounds(58, 206, 88, 16);
		contentPane.add(lblLogs);
		
		JLabel lblTestFile = new JLabel("Test File:");
		lblTestFile.setBounds(58, 234, 88, 16);
		contentPane.add(lblTestFile);
		
		}

	
	//@@@@@@@@@@@@@@@@@@@@@@ Method to show Language Combo Box @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//
	
	public void languageComboBoxMethod(){
	
	final JComboBox languageComboBox = new JComboBox();
	languageComboBox.setToolTipText("Please select the language in which the test file is written.");
	
	languageComboBox.setModel(new DefaultComboBoxModel(new String[] {"Java", ".Net", "JML", "Pharo"}));
	languageComboBox.setBounds(148, 90, 137, 27);
	contentPane.add(languageComboBox);
		
	languageComboBox.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if(languageComboBox.getSelectedItem().equals("Java")){
				language = "-java";
			} else if(languageComboBox.getSelectedItem().equals(".Net")){
				language = "-dotnet";
			}else if(languageComboBox.getSelectedItem().equals("JML")){
				language = "-jml";
			}
			else
				language = "-pharo";
			
		}
	});
	}
	
	
	
	//@@@@@@@@@@@@@@@@@@ Method to show Strategy Combo Box @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//
	
	public void strategyComboBoxMethod(){
		
		final JComboBox strategyComboBox = new JComboBox();
		strategyComboBox.setToolTipText("Please select the strategy for the current test session");
		strategyComboBox.setModel(new DefaultComboBoxModel(new String[] {"Random", "DSSR", "Random Plus", "DSSR Plus"}));
		strategyComboBox.setBounds(148, 118, 137, 27);
		contentPane.add(strategyComboBox);
		
		strategyComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(strategyComboBox.getSelectedItem().equals("Random")){
					strategy = "-random";
				}else if (strategyComboBox.getSelectedItem().equals("Random Plus")){
					strategy = "-randomPlus";
				} else if (strategyComboBox.getSelectedItem().equals("DSSR")){
					strategy = "-DSSR";
				}else
					strategy = "-DSSR2";
			
			}
		});
	}
	
	
	//@@@@@@@@@@@@@@@@@@@@@@@@ Method to show TWO Time Combo Box @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//
	
	public void timeComboBoxMethod(){
			
		
				time1ComboBox = new JComboBox();
				time1ComboBox.setToolTipText("Select test duration in numbers");
				
				time1ComboBox.setModel(new DefaultComboBoxModel(new String[] {"5", "10", "15", "20", "30", "40", "50", "60", "70", "80", "90", "100"}));
				time1ComboBox.setBounds(148, 146, 71, 27);
				contentPane.add(time1ComboBox);
				
				//@@@@@@@@@@@@@@@@@@@@@  Minutes or Second Combo Box @@@@@@@@@@@@@@@@@@@@@@@@@//
				
				time2ComboBox = new JComboBox();
				time2ComboBox.setToolTipText("Select test duration in Minutes or seconds");

				time2ComboBox.setModel(new DefaultComboBoxModel(new String[] {"Seconds", "Minutes"}));
				time2ComboBox.setBounds(214, 146, 108, 27);
				contentPane.add(time2ComboBox);
				
	}
	
	
	//@@@@@@@@@@@@@@@@@@@@@@@@ Method to show GUI Check Box @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//
	
		public void guiCheckBoxMethod(){
			
			GuiCheckBox = new JCheckBox("");
			GuiCheckBox.setToolTipText("Select it if you want to see the progress of test execution in GUI window");
			GuiCheckBox.setSelected(true);
			GuiCheckBox.setBounds(148, 174, 128, 23);
			contentPane.add(GuiCheckBox);
			
			GuiCheckBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				
				if (GuiCheckBox.isSelected())
				gui = "-gui";
				}
				});
		}
	
	
	//@@@@@@@@@@@@@@@@@@@@@@@ Method to show LOGS Check Box @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//
		
	public void logsCheckBoxMethod(){
	
		LogsCheckBox = new JCheckBox("");
		LogsCheckBox.setToolTipText("Select it if you want to see the logs.");
		LogsCheckBox.setBounds(148, 202, 128, 23);
		contentPane.add(LogsCheckBox);
		
		LogsCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		if (LogsCheckBox.isSelected()){
			logs = "-rawlogs";
		}
		else {
			logs = "-nologs";
		}
		} 
		
		});
		
	}
	
	
	//@@@@@@@@@@@@@@@@@@@@@@ Method to show Browse Button   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//
	
	public void browseButtonMethod(){
		
		JButton browseButton = new JButton("Browse");
		browseButton.setToolTipText("Select the file that you want to test in the current test session.");
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				File file = chooser.getSelectedFile();
				String fullPath = file.getAbsolutePath();
				char extentionSeperator = '.';
				char pathSeperator = '/';
				
				
				int sep = fullPath.lastIndexOf(pathSeperator); 
				
				testFilePath = "-yetiPath=" + fullPath.substring(0 , sep + 1);
				//FOLLOWING NOT WORKING FOR SOME REASON AND THE ABOVE WORKS. BIT STRANGE.
				//testFilePath = testFilePath+ ":" + fullPath.substring(0 , sep + 1);
				
				path = fullPath.substring(0, sep + 1);
				
				int dot = fullPath.lastIndexOf(extentionSeperator);
				int sept = fullPath.lastIndexOf(pathSeperator);
				fileName = fullPath.substring(sept+1,dot);
				
				
				
				txtItDisplayThe.setText(fileName);
				
				fileName = "-testModules="+fileName;
				
				
							
				//str = str.substring(0, str.lastIndexOf('.'));
				//str = "-testModules="+str;
				//fileName = (str.replaceAll("/", "."));
			}
		});
		browseButton.setBounds(148, 229, 117, 29);
		contentPane.add(browseButton);
		
		txtItDisplayThe = new JTextField();
		txtItDisplayThe.setText("yeti.test.YetiTest");
		txtItDisplayThe.setToolTipText("It display the path of the file that is selected for testing.");
		txtItDisplayThe.setBounds(151, 262, 349, 28);
		contentPane.add(txtItDisplayThe);
		txtItDisplayThe.setColumns(10);


	}
	

	//@@@@@@@@@@@@@@@@@@@@@@@@ Thread to start YETI @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//
		
	private class Threado implements Runnable{
        public void run(){
                try{
                        Yeti.YetiRun(command);
                }
                catch(Exception e){

                }
        }
}
	
		
	//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ RUN Test Button @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//
	
	public void runButtonMethod(){
		
				JButton runButton = new JButton("Run Yeti Test");
				runButton.setToolTipText("Click the button to start the test");
				runButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						time = "-time=" + time1ComboBox.getSelectedItem().toString();
						if(time2ComboBox.getSelectedItem().equals("Minutes")){
							time = time + "mn";
						} else {
							time = time + "s";
						}
						
						// @@@@@@@ Output the values to see correct or not @@@@@@ //
//						JOptionPane.showMessageDialog(null, language, " language is", JOptionPane.PLAIN_MESSAGE);
//						JOptionPane.showMessageDialog(null, time, " time is ", JOptionPane.PLAIN_MESSAGE);
//						JOptionPane.showMessageDialog(null, logs, " logs is ", JOptionPane.PLAIN_MESSAGE);
//						JOptionPane.showMessageDialog(null, strategy, " strategy is ",JOptionPane.PLAIN_MESSAGE);
//						JOptionPane.showMessageDialog(null, fileName, "fileName is", JOptionPane.PLAIN_MESSAGE);
//						JOptionPane.showMessageDialog(null, testFilePath, "testFilePath is", JOptionPane.PLAIN_MESSAGE);
						
						list.add(language);
						list.add(strategy);
						list.add(time);
						list.add(gui);
						list.add(logs);
						list.add(fileName);
						list.add(testFilePath);
						
						
						
										
						 command = list.toArray(new String[list.size()]);
						
						
						t1.start();
		           
						
					}
				});
				
				runButton.setBounds(58, 330, 444, 29);
				contentPane.add(runButton);
				
				JButton btnNewButton = new JButton("Count generated files");
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						String files;
						File folder = new File(path);
						File[] listOfFiles = folder.listFiles();
						
						for (int i=0; i < listOfFiles.length; i++)
						{
							if(listOfFiles[i].isFile()){
								files = listOfFiles[i].getName();
								if((files.startsWith("C"))&&(files.endsWith(".java"))){
									filesToCompile.add(files);
									
								}
								
							}
						}
						
								
						filesToCompileArray = filesToCompile.toArray(new String[filesToCompile.size()]);
												
//						for (int z = 0; z < filesToCompileArray.length; z++)
//						{
//							String temp1;
//							temp1 = filesToCompileArray[z];
//							// JOptionPane.showMessageDialog(null, temp1);
//						}

						textField.setText(filesToCompileArray.length + " files generated") ;
				
					}
					
					
				});
				btnNewButton.setBounds(58, 371, 207, 29);
				contentPane.add(btnNewButton);
				
				textField = new JTextField();
				textField.setBounds(273, 369, 227, 30);
				contentPane.add(textField);
				textField.setColumns(10);
				
				JButton btnNewButton_1 = new JButton("Compile Files");
				btnNewButton_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						int count = 0;
						for (int i = 0; i < filesToCompileArray.length; i ++){
							Process pro1;
							try {
//								String temp1 = "javac " + path + filesToCompileArray[i];
//								JOptionPane.showMessageDialog(null, temp1);
//								pro1 = Runtime.getRuntime().exec(temp1);
								pro1 = Runtime.getRuntime().exec("javac " + path + filesToCompileArray[i]);
								count = count + 1;
							} catch (IOException e) {
								
								e.printStackTrace();
							}
						    
						}
						
						textField_1.setText(count + " files compiled");
					}
				});
				btnNewButton_1.setBounds(58, 412, 207, 29);
				contentPane.add(btnNewButton_1);
				
				textField_1 = new JTextField();
				textField_1.setBounds(273, 411, 227, 28);
				contentPane.add(textField_1);
				textField_1.setColumns(10);
				
				
				
				
				//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Code to Execute C0, C1, .... files %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
				JButton btnNewButton_2 = new JButton("Execute Files");
				btnNewButton_2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						int count = 0;
						for (int i = 0; i < filesToCompileArray.length; i++){
							Process pro1;
							try {
//								String temp1 = "javac " + path + filesToCompileArray[i];
//								JOptionPane.showMessageDialog(null, temp1);
//								pro1 = Runtime.getRuntime().exec(temp1);
								pro1 = Runtime.getRuntime().exec("java " + path + filesToCompileArray[i]);
								count = count + 1;
							} catch (IOException e1) {
								
								e1.printStackTrace();
							}
						    
						}
						
						textField_3.setText(count + " files executed");
					}
				});
				btnNewButton_2.setBounds(58, 453, 207, 29);
				contentPane.add(btnNewButton_2);
				
				textField_3 = new JTextField();
				textField_3.setBounds(273, 452, 227, 28);
				contentPane.add(textField_3);
				textField_3.setColumns(10);
				
				
				//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% End  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
				
				
				
				JLabel lblNewLabel = new JLabel("Description of each field (Temporary)");
				lblNewLabel.setBounds(571, 94, 403, 16);
				contentPane.add(lblNewLabel);
				
				JLabel lblSelectStrategyFor = new JLabel("Select Strategy for the Test Session");
				lblSelectStrategyFor.setBounds(571, 122, 403, 16);
				contentPane.add(lblSelectStrategyFor);
				
				JLabel lblSelectDurationIn = new JLabel("Select duration in minutes or seconds");
				lblSelectDurationIn.setBounds(571, 150, 403, 16);
				contentPane.add(lblSelectDurationIn);
				
				JLabel lblCheckTheBox = new JLabel("Check the box to see GUI test session");
				lblCheckTheBox.setBounds(571, 178, 403, 16);
				contentPane.add(lblCheckTheBox);
				
				JLabel lblCheckTheBox_1 = new JLabel("Check the box to save the logs of test session");
				lblCheckTheBox_1.setBounds(571, 206, 403, 16);
				contentPane.add(lblCheckTheBox_1);
				
				JLabel lblSelectBinaryFile = new JLabel("Select binary file \".class\" to test");
				lblSelectBinaryFile.setBounds(571, 234, 403, 16);
				contentPane.add(lblSelectBinaryFile);
				
				JLabel lblDisplayTheName = new JLabel("Display the name of the selected file without extention");
				lblDisplayTheName.setBounds(571, 268, 403, 16);
				contentPane.add(lblDisplayTheName);
				
				JLabel lblClickToRun = new JLabel("Click to run YETI test on selected file ");
				lblClickToRun.setBounds(571, 335, 403, 16);
				contentPane.add(lblClickToRun);
				
				JLabel lblInCaseOf = new JLabel();
				lblInCaseOf.setText("<HTML>In DSSR Plus strategy, on click it counts number of generated files starting with name C in current folder and display it in text field</HTML>");
				lblInCaseOf.setBounds(571, 360, 423, 48);
				contentPane.add(lblInCaseOf);
				
				JLabel label = new JLabel();
				label.setText("<HTML>In DSSR Plus strategy, on click it compile the files starting with name C in current folder and display it in text field</HTML>");
				label.setBounds(571, 417, 423, 48);
				contentPane.add(label);
				
				JLabel label_1 = new JLabel("Select Language of the program under test");
				label_1.setBounds(571, 66, 403, 16);
				contentPane.add(label_1);
				
				JButton btnNewButton_3 = new JButton("click button to plot xy graph");
				btnNewButton_3.setBounds(58, 494, 444, 29);
				contentPane.add(btnNewButton_3);
				
				
				
				
			
	}
}




