package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bbc.juniperus.games.minesweeper.core.MineField;
import bbc.juniperus.games.minesweeper.gui.resources.ResourceManager;

public class MineFieldPane extends JPanel{

	private static final long serialVersionUID = 1L;

	public MineFieldPane(MineField field){
		super(new GridLayout(field.getWidth(),field.getHeight()));
		
		Controller controller = new Controller(field,this);
		
		for (int y = 0; y < field.getHeight(); y++)
			for (int x = 0; x < field.getWidth(); x++){
				CellGui view = new CellGui(field.getCellInfo(x, y));
				view.addPropertyChangeListener(controller);
				add(view);
				
			}
			
			
	}

	public void update() {
		for (Component c : this.getComponents())
			c.repaint();
	}
	


}
