package yeti.monitoring;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import yeti.Yeti;
import yeti.YetiModule;

/**
 * Class that represents a table with all modules loaded and the ones tested/nottested.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Sep 16, 2009
 *
 */
@SuppressWarnings("serial")
public class YetiModuleGraph extends JPanel {

	/**
	 * The model for the JTable
	 */
	public ModuleTableModel moduleTableModel = new ModuleTableModel();
	
	/**
	 * The button to add a module.
	 */
	public JButton addModule = new JButton("Add module to test");

	public YetiModuleGraph(int width, int height) {

		// we create the table
		JTable moduleTable = new JTable(moduleTableModel);
		// we set up layout info
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		moduleTable.getColumn("").setMaxWidth(25);
		moduleTable.setPreferredScrollableViewportSize(new Dimension(width, height));
		moduleTable.setFillsViewportHeight(true);
		// we encapsulate the table in a scroll pane
		JScrollPane scrollPane = new JScrollPane(moduleTable);
		this.setPreferredSize(new Dimension(250,0));
		scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		this.addModule.setAlignmentX(Component.CENTER_ALIGNMENT);

		// we add the two components
		this.add(scrollPane);
		this.add(this.addModule);
	}

	public ModuleTableModel getModel() {
		return moduleTableModel;
	}

	@SuppressWarnings("serial")
	class ModuleTableModel extends AbstractTableModel implements YetiUpdatable {
		private String[] columns = {"","Module Name"};
		private Object[][] values = null;

		public ModuleTableModel() {
			updateValues();
		}

		public void updateValues() {
			int nModules =  YetiModule.allModules.size();
			if ((values == null) || (nModules!=values.length)) {
				Object[] keys = YetiModule.allModules.keySet().toArray(); 
				values =  new Object[nModules][2];
				for (int i=0; i<nModules; i++) {
					values[i][0] = new Boolean(Yeti.testModule.containsModuleName((String)keys[i]));
					values[i][1] = keys[i];
				}
				this.fireTableDataChanged();
			}
		}

		public int getColumnCount() {
			return columns.length;
		}

		public int getRowCount() {
			return values.length;
		}

		public String getColumnName(int col) {
			return columns[col];
		}

		public Object getValueAt(int row, int col) {
			return values[row][col];
		}

		@SuppressWarnings("unchecked")
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		/*
		 * Don't need to implement this method unless your table's
		 * editable.
		 */
		public boolean isCellEditable(int row, int col) {
			//Note that the data/cell address is constant,
			//no matter where the cell appears onscreen.
			if (col < 1) {
				return true;
			} else {
				return false;
			}
		}

		/*
		 * Don't need to implement this method unless your table's
		 * data can change.
		 */
		public void setValueAt(Object value, int row, int col) {
			if (col == 0) {
				values[row][col] = value;
				fireTableCellUpdated(row, col);
			}
		}
	}

}
