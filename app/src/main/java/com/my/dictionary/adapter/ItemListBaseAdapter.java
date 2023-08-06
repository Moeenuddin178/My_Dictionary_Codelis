package com.my.dictionary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.my.dictionary.R;
import com.my.dictionary.model.Word;

import java.util.ArrayList;
import java.util.List;

public class ItemListBaseAdapter extends BaseAdapter implements Filterable {

	private Context contex;
	private static List<Word> original_items = new ArrayList<>();
	private static List<Word> filtered_items = new ArrayList<>();

	private ItemFilter mFilter = new ItemFilter();
	private LayoutInflater l_Inflater;

	public ItemListBaseAdapter(Context context, List<Word> results) {
		this.contex=context;
		original_items = results;
		filtered_items = results;
		l_Inflater = LayoutInflater.from(context);
	}

	public Filter getFilter() {
		return mFilter;
	}

	public int getCount() {
		return filtered_items.size();
	}

	public Object getItem(int position) {
		return filtered_items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.txt_word = (TextView) convertView.findViewById(R.id.textView1);
			holder.txt_result= (TextView) convertView.findViewById(R.id.textView2);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txt_word.setText(filtered_items.get(position).getWord());
		holder.txt_result.setText(filtered_items.get(position).getResult());
		return convertView;
	}

	static class ViewHolder {
		TextView txt_word;
		TextView txt_result;
	}


	private class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			String query = constraint.toString().toLowerCase();

			FilterResults results = new FilterResults();
			final List<Word> list = original_items;
			final List<Word> result_list = new ArrayList<>(list.size());

			for (int i = 0; i < list.size(); i++) {
				String str_word = list.get(i).getWord();
				String str_res = list.get(i).getResult();
				if (str_word.toLowerCase().contains(query) || str_res.toLowerCase().contains(query)) {
					result_list.add(list.get(i));
				}
			}

			results.values = result_list;
			results.count = result_list.size();

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			filtered_items = (List<Word>) results.values;
			notifyDataSetChanged();
		}

	}
}
