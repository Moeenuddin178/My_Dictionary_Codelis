package com.my.dictionary.theme;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.ActionBar;

import com.my.dictionary.data.GlobalVariable;

public class ActionBarColoring {
	private Activity act;
	private GlobalVariable global; 

	public ActionBarColoring(Activity act) {
		super();
		this.act = act;
		global = (GlobalVariable) act.getApplication();
	}
	
	public ActionBar getColor(ActionBar actionbar){
		ActionBar action_new = actionbar;
		ColorDrawable colordrw = new ColorDrawable(global.getIntColor());
		action_new.setBackgroundDrawable(colordrw);
		return action_new;
	}
}
