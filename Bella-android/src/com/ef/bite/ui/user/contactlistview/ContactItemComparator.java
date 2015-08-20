package com.ef.bite.ui.user.contactlistview;

import java.util.Comparator;

public class ContactItemComparator implements Comparator<ContactItemInterface> {

	@Override
	public int compare(ContactItemInterface lhs, ContactItemInterface rhs) {
		if(lhs.getItemForIndex() == null || rhs.getItemForIndex() == null)
			return -1;
		
		String lhs_1 = PinyinUtils.getPingYin(lhs.getItemForIndex());
		String rhs_1 = PinyinUtils.getPingYin(rhs.getItemForIndex());
		int result = lhs_1.compareToIgnoreCase(rhs_1);
		return result;
		
	}

}
