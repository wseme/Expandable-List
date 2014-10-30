package dev.seme;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class MultiEntryDialog extends Dialog {
	
	private String title;
	private String description;
	
	private Button okBtn, cancelBtn;
	
	private View view;
	
	private EditText content;

	public MultiEntryDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Constructor
	 * 
	 * @param context - Parent Context
	 * @param nEntryLayoutId - Entry LayoutId
	 * @param position - Array position (for Edit)
	 * @param title - Dialog Title
	 * @param listener - Data Exchange Interface
	 */
	public MultiEntryDialog(Context context,
			String title, String description) {
		super(context, android.R.style.Theme_Dialog);

		this.title= title;
		this.description = description;
		this.content = new EditText(context);//TODO remove and use for implementation
	}
	
	/**
     * Called when the dialog is first created. 
     *
     * @param savedInstanceState - Any saved data from 
     * 							   the previous state
     */
	@Override     
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);         
		setContentView(R.layout.multi_entry_dialog);          

		okBtn= (Button) findViewById(R.id.btn_ok); 
		cancelBtn = (Button) findViewById(R.id.btn_cancel); 

		content= (EditText) findViewById(R.id.edit_text); 

		okBtn.setOnClickListener(new View.OnClickListener() {
			/**
			 * Implemented to return data
			 */
			public void onClick(View v) {
				
				//hide keyboard
				InputMethodManager imm = (InputMethodManager) MultiEntryDialog.this
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						0);

			
				
				dismiss();
				
			}
        });

		cancelBtn.setOnClickListener(new View.OnClickListener() {
			/**
			 * Implemented to cancel
			 */
        	public void onClick(View v) {
        		
        		//hide keyboard
				InputMethodManager imm = (InputMethodManager) MultiEntryDialog.this
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						0);
				
				dismiss();
        	}
        });
		
	}	

	public String getContentValue(){
		return 	content.getText().toString();
	}
	public void setContentValue(String value){
		if(value != null && content != null){
			content.setText(value);
		}
	}
}
