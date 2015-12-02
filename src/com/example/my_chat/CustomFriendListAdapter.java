package com.example.my_chat;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomFriendListAdapter extends BaseAdapter {

	ArrayList<FriendListData> friendDetails = new ArrayList<FriendListData>();
	LayoutInflater inflater;
	Context context;

	public CustomFriendListAdapter(Context context, ArrayList<FriendListData> friendDetails) {
		this.friendDetails = friendDetails;
		this.context = context;
		inflater = LayoutInflater.from(this.context);
	}

	@Override
	public int getCount() {
		return friendDetails.size();
	}

	@Override
	public Object getItem(int position) {

		return friendDetails.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		MyViewHolder mHolder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.custom_friends_list_view, parent, false);

			mHolder = new MyViewHolder(convertView);
			convertView.setTag(mHolder);
		} else {
			mHolder = (MyViewHolder) convertView.getTag();
		}
		
		FriendListData data = (FriendListData) getItem(position);
		
		mHolder.name.setText(data.getFullName());
		mHolder.uname.setText(data.getUname());
		mHolder.profilePic.setImageResource(data.getImgResId());

		return convertView;
	}

	private class MyViewHolder {
		TextView name, uname;
		ImageView profilePic;

		public MyViewHolder(View item) {
			name = (TextView) item.findViewById(R.id.fullName);
			uname = (TextView) item.findViewById(R.id.uname);
			profilePic = (ImageView) item.findViewById(R.id.profilePic);
		}
	}

}
