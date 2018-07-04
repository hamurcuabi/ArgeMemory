package com.emrehmrc.argememory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.emrehmrc.argememory.R;
import com.emrehmrc.argememory.activity.UploadActivity;
import com.emrehmrc.argememory.model.ShareModel;
import com.emrehmrc.argememory.model.SingletonShare;
import com.emrehmrc.argememory.popup.CommentPopup;
import com.emrehmrc.argememory.popup.SCommentPopup;
import com.emrehmrc.argememory.popup.SShareMemberPopup;
import com.emrehmrc.argememory.popup.SShareTagPopup;
import com.emrehmrc.argememory.popup.SUpdateDescpPopup;
import com.emrehmrc.argememory.popup.ShareMemberPopup;
import com.emrehmrc.argememory.popup.ShareTagPopup;
import com.emrehmrc.argememory.popup.UpdateDescpPopup;

import java.util.ArrayList;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.MyviewHolder> implements Filterable {

    ArrayList<ShareModel> datalist;
    LayoutInflater layoutInflater;
    Context mContentxt;
    ShareFilterAdapter filter;
    FragmentManager mfragmentManager;


    public ShareAdapter(Context context, ArrayList<ShareModel> data, FragmentManager fragmentManager) {
        layoutInflater = LayoutInflater.from(context);
        this.datalist = data;
        this.mContentxt = context;
        this.mfragmentManager = fragmentManager;

    }


    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //   View v = layoutInflater.inflate(R.layout.shareallrecycler, parent, false);
        View v = LayoutInflater.from(mContentxt).inflate(R.layout.shareallrecycler, parent, false);
        MyviewHolder myViewHolder = new MyviewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyviewHolder holder, final int position) {

        holder.setIsRecyclable(false);
        ShareModel clicked = datalist.get(position);
        holder.setData(clicked, position);


    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ShareFilterAdapter(this, datalist);
        }

        return filter;
    }

    private void openUpdateDescp(String id, String descp) {

        SingletonShare singletonShare = SingletonShare.getInstance();
        singletonShare.setSharedId(id);
        singletonShare.setOldDescp(descp);
        SUpdateDescpPopup updateDescpPopup = new SUpdateDescpPopup();
        updateDescpPopup.show(mfragmentManager, "Açıklama " +
                "Güncelleme");


    }


    public class MyviewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView txtDate, txtOwner, txtTag, txtTotalMember, txtDescrp, txtTagImage, txtDescpS,
                txtComment;
        ImageButton imgMembers;


        public MyviewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.tvTaskDate);
            txtOwner = itemView.findViewById(R.id.tvTaskCreater);
            txtTag = itemView.findViewById(R.id.tvTaskTag);
            txtTotalMember = itemView.findViewById(R.id.tvTaskTotalMan);
            txtDescrp = itemView.findViewById(R.id.tvDescription);
            imgMembers = itemView.findViewById(R.id.imgTaskMans);
            txtTagImage = itemView.findViewById(R.id.txtTagS);
            txtDescpS = itemView.findViewById(R.id.txtDescpS);
            txtComment=itemView.findViewById(R.id.txtCommnet);
            cardView=itemView.findViewById(R.id.rootCardview);


        }

        @SuppressLint("NewApi")
        public void setData(final ShareModel clicked, int position) {
            this.txtDate.setText(clicked.getShareDate().substring(0, 16));
            this.txtOwner.setText(clicked.getShareOwner());
            this.txtDescrp.setText(clicked.getShareDescp());
            this.txtTag.setText(clicked.getShareTag() + " Adet Etiketli");
            this.txtTotalMember.setText(clicked.getShareCountMember());
            this.txtComment.setText("  "+clicked.getShareCountComment());

            imgMembers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContentxt, SShareMemberPopup.class);
                    i.putExtra("id", clicked.getShareID());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContentxt.startActivity(i);

                }
            });
            txtTagImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContentxt, SShareTagPopup.class);
                    i.putExtra("id", clicked.getShareID());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContentxt.startActivity(i);
                }
            });
            txtDescpS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openUpdateDescp(clicked.getShareID(), clicked.getShareDescp());
                }
            });
            txtComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContentxt, SCommentPopup.class);
                    i.putExtra("id", clicked.getShareID());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContentxt.startActivity(i);
                }
            });
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    SingletonShare singletonShare=SingletonShare.getInstance();
                    singletonShare.setFileSharedId(clicked.getShareID());

                    Intent i = new Intent(mContentxt, UploadActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContentxt.startActivity(i);
                    return true;
                }
            });

        }
    }
}
