package com.ef.bite.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ef.bite.R;
import com.ef.bite.dataacces.mode.httpMode.HttpGetFriendData;
import com.ef.bite.ui.user.contactlistview.ContactItemInterface;
import com.ef.bite.ui.user.contactlistview.ContactsSectionIndexer;
import com.ef.bite.utils.AvatarHelper;
import com.ef.bite.utils.FontHelper;

public class ContactListAdapter extends ArrayAdapter<ContactItemInterface> {

	private int resource; // store the resource layout id for 1 row
	private boolean inSearchMode = false;
	private Context mContext;

	private ContactsSectionIndexer indexer = null;
	LayoutInflater inflater;

	public ContactListAdapter(Context _context, int _resource,
			List<ContactItemInterface> _items) {
		super(_context, _resource, _items);
		this.mContext = _context;
		resource = _resource;
		inflater = (LayoutInflater) _context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setIndexer(new ContactsSectionIndexer(_items));

	}

	// get the section textview from row view
	// the section view will only be shown for the first item
	public TextView getSectionTextView(View rowView) {
		TextView sectionTextView = (TextView) rowView
				.findViewById(R.id.friend_list_item_sectionTextView);
		return sectionTextView;
	}

	public void showSectionViewIfFirstItem(View rowView,
			ContactItemInterface item, int position) {
		TextView sectionTextView = getSectionTextView(rowView);

		// if in search mode then dun show the section header
		if (inSearchMode) {
			sectionTextView.setVisibility(View.GONE);
		} else {
			// if first item then show the header

			if (indexer.isFirstItemInSection(position)) {

				String sectionTitle = indexer.getSectionTitle(item
						.getItemForIndex());
				sectionTextView.setText(sectionTitle);
				sectionTextView.setVisibility(View.VISIBLE);

			} else
				sectionTextView.setVisibility(View.GONE);
		}
	}

	// this should be override by subclass if necessary
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContactItemInterface item = getItem(position);
		HolderView holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(resource, null, false);
			holder = new HolderView();
			holder.name = (TextView) convertView
					.findViewById(R.id.friend_list_item_name);
			holder.image = (ImageView) convertView
					.findViewById(R.id.friend_list_item_avatar);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		// for the very first section item, we will draw a section on top
		showSectionViewIfFirstItem(convertView, item, position);
		holder.name.setText(item.getItemForIndex());
		if (item != null && item instanceof HttpGetFriendData) {
			HttpGetFriendData contact = (HttpGetFriendData) item;
			if (contact.avatar != null && !contact.avatar.isEmpty()) {
				AvatarHelper.LoadAvatar(holder.image, contact.bella_id,
						contact.avatar);
			}
		}
		FontHelper.applyFont(getContext(), convertView,
				FontHelper.FONT_Museo300);
		return convertView;
	}

	public boolean isInSearchMode() {
		return inSearchMode;
	}

	public void setInSearchMode(boolean inSearchMode) {
		this.inSearchMode = inSearchMode;
	}

	public ContactsSectionIndexer getIndexer() {
		return indexer;
	}

	public void setIndexer(ContactsSectionIndexer indexer) {
		this.indexer = indexer;
	}

	static class HolderView {
		TextView name;
		ImageView image;
	}

}
