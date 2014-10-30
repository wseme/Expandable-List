package dev.seme;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MultiEntryAdapter extends BaseExpandableListAdapter {
	/*-------------------------- Fields --------------------------*/

	private final HashMap<String, ArrayList<String>> myData = new HashMap<String, ArrayList<String>>();
	private final HashMap<Integer, String> lookUp = new HashMap<Integer, String>();// id
																					// to
																					// name
	private final HashMap<Integer, String> description = new HashMap<Integer, String>();// id
																						// to
																						// description
	private final Context context;

	private MultiEntryDialog childDialog;
//	private MultiEntryDialog editChildDialog;

//	private String selectedGroupChild;// TODO change to int to use other;
//
//	public int selectedGroupPos;
//	public int selectedChildPos;

	/*-------------------------- Public --------------------------*/

	public MultiEntryAdapter(final Context con) {
		context = con;
		childDialog = new MultiEntryDialog(con);// TODO add from input or set
													// for children

	}

	public boolean AddGroup(final String groupName,
			final String groupDescription, final ArrayList<String> list) {
		final ArrayList<String> prev = myData.put(groupName, list);

		if (prev != null)
			return false;

		lookUp.put(myData.size() - 1, groupName);
		description.put(myData.size() - 1, groupDescription);

		notifyDataSetChanged();
		return true;
	}

	@Override
	public Object getChild(int groupPos, int childPos) {
		if (lookUp.containsKey(groupPos)) {
			final String str = lookUp.get(groupPos);
			final ArrayList<String> data = myData.get(str);

			return data.get(childPos);
		}

		return null;
	}

	@Override
	public long getChildId(int groupPos, int childPos) {
		return 0;
	}

	@Override
	public View getChildView(int groupPos, int childPos, boolean isLastChild,
			View convertView, ViewGroup parent) {
		LinearLayout linear = new LinearLayout(context);

		final LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);

		RelativeLayout row = new RelativeLayout(context);

		TextView text = new TextView(context);

		// Indent
		final String str = "\t\t\t" + (String) getChild(groupPos, childPos);

		linear = new LinearLayout(context);
		linear.setOrientation(LinearLayout.VERTICAL);

		text.setLayoutParams(params);
		text.setPadding(0, 10, 0, 10);
		text.setText(str);
		text.setTextColor(Color.GRAY);
		text.setTextSize(25);
		row.addView(text);

		ImageView image = new ImageView(context);

		RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
				60, LayoutParams.FILL_PARENT);
		imageParams.setMargins(0, 0, 28, 10);
		imageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		image.setLayoutParams(imageParams);
		image.setImageDrawable(context.getResources().getDrawable(
				R.drawable.remove));
		image.setClickable(true);
		image.setScaleType(ScaleType.FIT_CENTER);
		image.setVisibility(View.VISIBLE);

		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// get the row the clicked button is in
				RelativeLayout vwParentRow = (RelativeLayout) v.getParent();
				TextView text =(TextView)vwParentRow.getChildAt(0);
				
				
				for(int groupIndex = 0; groupIndex < getGroupCount(); groupIndex++){					
					for(int childIndex = 0; childIndex < getChildrenCount(groupIndex); childIndex++){
						
						//iterate over all the children
						//if child == view selected, remove it
						
						String childValue = (String) getChild(groupIndex,
								childIndex);
						String textValue = text.getText().toString();
						
						if(textValue.trim().equalsIgnoreCase(childValue.trim())){	
							 removeChild(groupIndex, childIndex );
							 break;
						}
					}
					
				}
			}

		});

		text.setOnLongClickListener(new EditClick());
		image.setOnLongClickListener(new EditClick());

		RelativeLayout.LayoutParams rowParams = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		rowParams.addRule(RelativeLayout.ALIGN_LEFT, text.getId());
		rowParams.addRule(RelativeLayout.ALIGN_RIGHT, image.getId());

		row.setLayoutParams(rowParams);
		row.addView(image);

		linear.addView(row);

		return linear;
	}

	private class EditClick implements OnLongClickListener {
		private int groupIndex;
		private int childIndex;
		
		@Override
		public boolean onLongClick(View v) {
			childDialog = new MultiEntryDialog(context);

			RelativeLayout vwParentRow = (RelativeLayout) v.getParent();
			TextView text = (TextView) vwParentRow.getChildAt(0);

			setTitleToGroupNameAndSetGroupPos(text);// TODO change later

			// set content value and get child position
			for (childIndex = 0; childIndex < getChildrenCount(groupIndex); childIndex++) {

				String oldChildValue = (String) getChild(groupIndex, childIndex);
				String textValue = text.getText().toString().trim();

				if (textValue.equalsIgnoreCase(oldChildValue.trim())) {
					childDialog.setContentValue(textValue);
					break;
				}
			}
			// TODO set Content value to child value
			childDialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					String value = childDialog.getContentValue();
					if (value != null && !value.equals("")) {
						editChild(groupIndex, childIndex, value);// update
																	// the
																	// value
																	// if
																	// not
																	// the
																	// same
					}
				}

			});
			childDialog.show();// display the dialog

			return false;
		}
		
		/**
		 * sets the dialog box title to the group name
		 * stores the groupIndex to the member variable
		 * @param text
		 */
		private void setTitleToGroupNameAndSetGroupPos(TextView text){
			//set group name and get the group position
			for(groupIndex = 0; groupIndex < getGroupCount(); groupIndex++){					
				Object array = getGroup(groupIndex);
				if (array instanceof ArrayList<?>) {
					//TODO change, too lazy atm
					@SuppressWarnings("unchecked")
					ArrayList<String> groupValue = (ArrayList<String>) array;
					String textValue = text.getText().toString().trim();
					for (String s : groupValue) {

						if (textValue.equalsIgnoreCase(s.trim())) {
							childDialog.setTitle("Editing " + textValue);
							return;
						}
					}
				}
			}
		}
	}

	@Override
	public int getChildrenCount(int groupPos) {
		if (lookUp.containsKey(groupPos))
			return myData.get(lookUp.get(groupPos)).size();

		return 0;
	}

	public void setAddDialog(MultiEntryDialog dialog) {
		if (dialog != null) {
			childDialog = dialog;
		}
	}

	@Override
	public Object getGroup(int groupPos) {
		if (lookUp.containsKey(groupPos))
			return myData.get(lookUp.get(groupPos));

		return null;
	}

	@Override
	public int getGroupCount() {
		return myData.size();
	}

	@Override
	public long getGroupId(int groupPos) {
		return -1;
	}

	@Override
	public View getGroupView(int groupPos, boolean isExpanded,
			View convertView, ViewGroup parent) {

		LinearLayout rowsLayout = new LinearLayout(context);
		rowsLayout.setOrientation(LinearLayout.VERTICAL);

		RelativeLayout topRow = new RelativeLayout(context);
		TextView groupText = new TextView(context);
		TextView descriptionText = new TextView(context);
		ImageView addImage = new ImageView(context);

		// Push the group name slightly to the right for drop down icon
//		selectedGroupPos = groupPos;
//		selectedGroupChild = lookUp.get(groupPos);
		final String groupStr = "\t\t" + lookUp.get(groupPos);
		final String descriptionStr = "\t\t" + description.get(groupPos);

		groupText.setText(groupStr);
		groupText.setTextColor(Color.WHITE);
		groupText.setTextSize(32);

		descriptionText.setText(descriptionStr);
		descriptionText.setTextColor(Color.WHITE);
		descriptionText.setTextSize(16);

		RelativeLayout.LayoutParams addImageParams = new RelativeLayout.LayoutParams(
				60, LayoutParams.FILL_PARENT);

		addImageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		addImageParams.addRule(RelativeLayout.ALIGN_RIGHT, addImage.getId());

		addImage.setLayoutParams(addImageParams);
		addImage.setImageResource(R.drawable.add);
		addImage.setClickable(true);
		addImage.setVisibility(View.VISIBLE);

		addImage.setOnClickListener(new OnClickListener() {

			private String selectedGroupChild;
			@Override
			public void onClick(View v) {
				if (childDialog != null) {
					// custom dialog

					RelativeLayout vwParentRow = (RelativeLayout) v.getParent();
					TextView text = (TextView) vwParentRow.getChildAt(0);
					
					selectedGroupChild = text.getText().toString().trim();
					
					childDialog.setTitle("Add an item");
					childDialog.setContentValue("");// reset value for now
					childDialog
							.setOnDismissListener(new OnDismissListener() {

								@Override
								public void onDismiss(DialogInterface dialog) {
									String value = childDialog
											.getContentValue();
									if (value != null && !value.equals("")) {
										addChild(selectedGroupChild, value);
									}
								}

							});
					childDialog.show();
				}
			}

		});

		topRow.addView(groupText);
		topRow.addView(addImage);

		rowsLayout.addView(topRow);
		rowsLayout.addView(descriptionText);

		return rowsLayout;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPos, int childPos) {
		return false;
	}

	private void addChild(String groupChild, String child) {
		if (myData.containsKey(groupChild)) {
			ArrayList<String> children = myData.get(groupChild);

			if (!children.contains(child)) {
				children.add(child);
				notifyDataSetChanged();
			}
		}
	}

	/**
	 * Removes inputted child, returns removed child string
	 * 
	 * @param groupPos
	 * @param childPos
	 * @return
	 */
	private void removeChild(int groupPos, int childPos) {
		if (lookUp.containsKey(groupPos)) {
			ArrayList<String> children = myData.get(lookUp.get(groupPos));
			children.remove(childPos);
			notifyDataSetChanged();
		}
	}

	private void editChild(int groupPos, int childPos, String value) {
		if (lookUp.containsKey(groupPos)) {
			ArrayList<String> children = myData.get(lookUp.get(groupPos));
			String child = children.get(childPos);
			if (child != null) {
				children.remove(childPos);
				children.add(childPos,value);
				notifyDataSetChanged();
			}

		}
	}
}