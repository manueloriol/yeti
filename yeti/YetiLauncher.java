package yeti;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class YetiLauncher extends JFrame {

        private String[] langArray = {"-java", "-.Net", "-JML"};
        private JComboBox timeComboBox;
        private String[] timeArray = {"5", "10", "15", "20", "30", "40","50","60", "70", "80", "90", "100", "150", "200", "300", "400", "500", "1000"};
        private String[] timeArray1 = {"mn", "sec"};
        private String[] strategy = {"-randomPlus", "-randomPlusPeriodic", "-randomPlusDecreasing"};
        private JLabel headingLabel;
        private JLabel langLabel;
        private JComboBox setLanguage;
        private JLabel timeLabel;
        private JComboBox setTime;
        private JComboBox setMinSec;
        private JLabel guiLabel;
        private JCheckBox guiSession;
        private JLabel logsLabel;
        private JCheckBox testLogs;
        private JLabel selectFilesLabel;
        private JButton selectFiles;
        private JButton runTest;
        private String[] command = new String[6];
        private String fileName;
        private JTextField fileNameTextField;
        private JButton exit;
        private JLabel strategyLabel;
        private JComboBox setStrategy;

        //private BrowseButtonHandler browseBHandler;
        private RunButtonHandler runBHandler;
        private ExitButtonHandler exitBHandler;


        public YetiLauncher () {
                setLayout(new GridBagLayout());

                GridBagConstraints c = new GridBagConstraints(); // for object position.


                setTitle("YETI-York Extensible Testing Infrastructure");
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setVisible(true);
                setSize(500,500);
                setLocation(400,150);



               headingLabel = new JLabel("York Extensible Testing Infrastructure");


                c.gridx = 0;            //object x axis.
                c.gridy = 0;            //object y axis.
                c.gridwidth  = 4;     //row span but not working for now
                c.gridheight = 2;     //col span but not working for now.
                c.insets = new Insets(15,15,15,15); //cell padding in pixels
                //c.anchor = GridBagConstraints.PAGE_END;
                headingLabel.setFont(new Font("Sarif", Font.BOLD, 15));
                add(headingLabel, c);

                langLabel = new JLabel("Test Language:");
                c.gridx = 1;
                c.gridy = 2;
                c.gridwidth = 1;
                c.gridheight =1;
                c.insets = new Insets(15,15,15,15);
                add(langLabel, c);

                setLanguage = new JComboBox(langArray);
                c.gridx = 2;
                c.gridy = 2;
                c.insets = new Insets(15,15,15,15);
                setLanguage.setSelectedIndex(0);
                add(setLanguage, c);

                strategyLabel = new JLabel("Test Strategy");
                c.gridx = 1;
                c.gridy = 3;
                c.gridwidth = 1;
                c.gridheight =1;
                c.insets = new Insets(15,15,15,15);
                add(strategyLabel, c);

                setStrategy = new JComboBox(strategy);
                c.gridx = 2;
                c.gridy = 3;
                c.gridwidth = 2;
                c.gridheight =1;
                c.insets = new Insets(15,15,15,15);
                setStrategy.setSelectedIndex(0);
                add(setStrategy, c);    
                
                
                timeLabel = new JLabel("Test Duration:");
                c.gridx = 1;
                c.gridy = 4;
                c.gridwidth = 1;
                c.gridheight =1;
                c.insets = new Insets(15,15,15,15);
                add(timeLabel, c);

                timeComboBox = new JComboBox(timeArray);
                c.gridx = 2;
                c.gridy = 4;
                //c.insets = new Insets(15,15,15,15);
                timeComboBox.setSelectedIndex(2);
                add(timeComboBox, c);

                setMinSec = new JComboBox(timeArray1);
                c.gridx = 3;
                c.gridy = 4;
                //c.insets = new Insets(15,15,15,15);
                setMinSec.setSelectedIndex(0);
                add(setMinSec, c);

                guiLabel = new JLabel("GUI test Session");
                c.gridx = 1;
                c.gridy = 5;
                c.insets = new Insets(15,15,15,15);
                add(guiLabel, c);

                guiSession = new JCheckBox("",null, true);
                c.gridx = 2;
                c.gridy = 5;
                c.insets = new Insets(15,15,15,15);
                add(guiSession, c);

                logsLabel = new JLabel("Save Test Logs");
                c.gridx = 1;
                c.gridy = 6;
                c.insets = new Insets(15,15,15,15);
                add(logsLabel, c);

                testLogs = new JCheckBox("",null, false);
                c.gridx = 2;
                c.gridy = 6;
                c.insets = new Insets(15,15,15,15);
                add(testLogs, c);

                selectFilesLabel = new JLabel("Select Test Files");
                c.gridx = 1;
                c.gridy = 7;
                c.insets = new Insets(15,15,15,15);
                add(selectFilesLabel, c);

                                
                fileNameTextField = new JTextField("yeti.test.YetiTest", 15);
                c.gridx = 2;
                c.gridy = 7;
                c.gridwidth = 2;
                c.gridheight =1;
                c.insets = new Insets(15,15,15,15);
                add(fileNameTextField, c);
                

                runTest = new JButton("Run Test");
                c.gridx = 2;
                c.gridy = 8;
                c.gridwidth = 1;
                c.gridheight =1;
                c.insets = new Insets(15,15,15,15);
                add(runTest, c);
                runBHandler = new RunButtonHandler();
                runTest.addActionListener(runBHandler);
                
                exit = new JButton("Exit Test");
                c.gridx = 3;
                c.gridy = 8;
                c.insets = new Insets(15,15,15,15);
                add(exit, c);
                exitBHandler = new ExitButtonHandler();
                exit.addActionListener(exitBHandler);


                pack(); // pack method collect the scattered objects on gui to one point.

        }

        public static void main(String[] args){

                YetiLauncher gui = new YetiLauncher();


        }

                private class RunButtonHandler implements ActionListener{

                        public void actionPerformed(ActionEvent e) {

                                command[0]= (String) (setLanguage.getSelectedItem());
                                String str1, str2, str3, time;
                                str1 = "-time=";
                                str2 = (String)timeComboBox.getSelectedItem();
                                str3 = (String)setMinSec.getSelectedItem();
                                time = str1+str2+str3;
                                command[1]= time;

                                String str4, str5, path;
                                str4 = "-testModules=";
                                //str5 = "yeti.test.YetiTest";
                                str5 = fileNameTextField.getText();
                                path = str4+str5;
                                command[2]= path;


                                if(guiSession.isSelected())
                                        command[3]= "-gui";
                                if(testLogs.isSelected())
                                        command[4]= "-logs";
                                else command[4]= "-nologs";
                                
                                command[5] = (String) (setStrategy.getSelectedItem());
                                 

                                for (int i =0; i<command.length; i++)
                                        System.out.print(command[i]);

                                Yeti.YetiRun(command);
                                

                        }
                }

                private class ExitButtonHandler implements ActionListener{

                    public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                } 
                
                
            }
}

