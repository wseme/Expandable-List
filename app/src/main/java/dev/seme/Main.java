package dev.seme;

import java.util.ArrayList;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

public class Main extends ExpandableListActivity
{
	private MultiEntryAdapter gea;
    private ExpandableListView elv;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        gea = new MultiEntryAdapter(this);
        
        ArrayList<String> models = new ArrayList<String>();
        models.add("army");
        models.add("navy");
        models.add("airforce");
        gea.AddGroup("Military","forces to attack or defend with", models);
       
        //XXX NOTE uses, hashmap, no duplicates allowed
        models = new ArrayList<String>();
        models.add("Evo");
        models.add("Sti");
        models.add("S4");
        models.add("Z4");
        models.add("GTR");
        models.add("C7");
        gea.AddGroup("Cars","methods of travel", models);
        
        setListAdapter(gea);
        elv = getExpandableListView();
//        elv.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        elv.setTextFilterEnabled(true);
        
        
     

    }
}