package com.baidu.ar.pro.ChatRoom;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.ar.pro.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private List<Friend> mFriendList;

    private MsgAdapter msgAdapter;

    private TextView chatroomName;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout Layout;

        ImageView friendImage;

        TextView chatroomName;

        TextView friendName;

        Button friendButton;

        public ViewHolder(View view) {
            super(view);
            Layout = view.findViewById(R.id.friend_list);
            friendName = view.findViewById(R.id.friend_name);
            friendImage = view.findViewById(R.id.friend_image);
            chatroomName = view.findViewById(R.id.chatroom_name);
            friendButton = view.findViewById(R.id.friend_button);
        }
    }

    public FriendAdapter(List<Friend> friendList, MsgAdapter msgAdapter, TextView chatroomName) {
        mFriendList = friendList;
        this.chatroomName = chatroomName;
        this.msgAdapter = msgAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Friend friend = mFriendList.get(position);
        holder.friendName.setText(friend.getName());
        holder.friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatroomName.setText("和" + holder.friendName.getText() + "的聊天室");
                Msg msg1 = new Msg("这里是你和" + holder.friendName.getText() + "的聊天记录", Msg.TYPE_RECEIVED);
                int size = msgAdapter.mMsgList.size();
                while(size-- != 0){
                    msgAdapter.mMsgList.remove(0);
                    msgAdapter.notifyItemRemoved(0);
                }
                msgAdapter.mMsgList.add(msg1);
                msgAdapter.notifyItemInserted(0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }

}
