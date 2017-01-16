package com.currency.viever;
import javax.swing.SwingUtilities;

public class Open {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new EXwindow();
			}
		});
	}
}
