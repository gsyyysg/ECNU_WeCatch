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
import android.widget.Toast;

import com.baidu.ar.pro.R;
import com.baidu.ar.pro.User;

import org.litepal.LitePal;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private List<User> mFriendList;

    private MsgAdapter msgAdapter;

    private TextView chatroomName;

    private int user_id;

    private int friend_id;

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
            chatroomName = view.findViewById(R.id.chatroomNameText);
            friendButton = view.findViewById(R.id.friend_button);
        }
    }

    public FriendAdapter(List<User> friendList, MsgAdapter msgAdapter, TextView chatroomName) {
        mFriendList = friendList;
        this.chatroomName = chatroomName;
        this.msgAdapter = msgAdapter;

        user_id = LitePal.where("owner = ?", "1").find(User.class).get(0).getUser_ID();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User friend = mFriendList.get(position);
        holder.friendName.setText(friend.getNickname());
        holder.friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatroomName.setText("和" + holder.friendName.getText() + "的聊天室");

                User friend = LitePal.where("nickname like ?", holder.friendName.getText().toString()).find(User.class).get(0);
                friend_id = friend.getUser_ID();

                int size = msgAdapter.mMsgList.size();
                while(size-- != 0){
                    msgAdapter.mMsgList.remove(0);
                    msgAdapter.notifyItemRemoved(0);
                }

                msgAdapter.mMsgList = LitePal.where("(sender_id like ? and receiver_id like ?) or (sender_id like ? and receiver_id like ?)",
                        Integer.toString(user_id), Integer.toString(friend_id),
                        Integer.toString(friend_id), Integer.toString(user_id))
                        .find(Message.class);
                msgAdapter.notifyItemRangeInserted(0, msgAdapter.mMsgList.size());


                //切换到与这个好友的消息记录
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }

    public int getFriend_id(){
        return friend_id;
    }

}
