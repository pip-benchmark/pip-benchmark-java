package org.pipbenchmark.gui.result;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class ResultsView extends Composite implements IResultsView {
	private IResultsViewListener _listener = null;
	private Text _contentText;
	
	public ResultsView(Composite parent) {
		super(parent, SWT.NONE);
		
		initializeComponent();
	}
	
	private void initializeComponent() {
		FormLayout layout = new FormLayout();
		this.setLayout(layout);
		
		Button saveButton = new Button(this, SWT.PUSH);
		saveButton.setText("Save...");
		saveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.saveReportClicked();
				}
			}
		});
		FormData data = new FormData();
		data.left = new FormAttachment(100, -80);
		data.right = new FormAttachment(100, -10);
		data.bottom = new FormAttachment(100, - 10);
		saveButton.setLayoutData(data);
		
		Button printButton = new Button(this, SWT.PUSH);
		printButton.setText("Print...");
		printButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.printReportClicked();
				}
			}
		});
		data = new FormData();
		data.left = new FormAttachment(100, -155);
		data.right = new FormAttachment(100, -85);
		data.bottom = new FormAttachment(100, - 10);
		printButton.setLayoutData(data);
		
		_contentText = new Text(this, SWT.BORDER | SWT.WRAP | SWT.MULTI
			| SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
		_contentText.setBackground(
				this.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		_contentText.setForeground(
				this.getDisplay().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
		Font contentFont = new Font(this.getDisplay(), "Courier New", 10, SWT.NORMAL);
		_contentText.setFont(contentFont);
		data = new FormData();
		data.top = new FormAttachment(0, 10);
		data.left = new FormAttachment(0, 10);
		data.right = new FormAttachment(100, -10);
		data.bottom = new FormAttachment(saveButton, -5);
		_contentText.setLayoutData(data);		
	}

    public String getReportContent() {
    	return _contentText.getText();
    }
    
    public void setReportContent(String value) {
    	_contentText.setText(value);
    	_contentText.setSelection(value.length());
    	_contentText.showSelection();
    }
    
    public String getSelectedReportContent() {
    	return _contentText.getSelectionText();
    }

    public void setListener(IResultsViewListener listener) {
    	_listener = listener;
    }
}
