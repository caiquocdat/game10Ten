package bo.ae.gameten10;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bo.ae.gameten10.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>  {
    private Context context;
    private ArrayList<Integer> pointsList;

    public HistoryAdapter(Context context, ArrayList<Integer> pointsList) {
        this.context = context;
        this.pointsList = pointsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_ten_rcv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int point = pointsList.get(position);
        holder.pointTv.setText(point +" POINT");
        holder.sttTv.setText(position+1+"");
        if(position%2==0){
            holder.itemRel.setBackgroundColor(Color.parseColor("#00000000"));
        }else {
            holder.itemRel.setBackgroundColor(Color.parseColor("#33FFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        return pointsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView pointTv,sttTv;
        RelativeLayout itemRel;
        ImageView topImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pointTv = itemView.findViewById(R.id.pointTv);
            sttTv = itemView.findViewById(R.id.sttTv);
            itemRel = itemView.findViewById(R.id.itemRel);
        }
    }
}
