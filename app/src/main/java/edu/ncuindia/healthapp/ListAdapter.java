package edu.ncuindia.healthapp;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private Context context;
    List<ItemModal> itemModalList;
    TextView tv;

    public float getTotal(){
        float total = 0;
        for(int i=0;  i<itemModalList.size(); i++){
            total += (itemModalList.get(i).getCalories() / itemModalList.get(i).getPortion_Default() * itemModalList.get(i).getConsumed());
        }
        return total;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView item_name;
        public TextView consumed;
        public ImageView image;
        public CardView card_view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.item_name = itemView.findViewById(R.id.item_name);
            this.consumed = itemView.findViewById(R.id.consumed);
            this.image = itemView.findViewById(R.id.image);
            this.card_view = itemView.findViewById(R.id.card_view);
        }
    }

    public ListAdapter(List<ItemModal> itemModalList, TextView tv) {
        this.itemModalList = itemModalList;
        this.tv = tv;
    }

    public ListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_layout, null);
        MyViewHolder vh = new MyViewHolder(view);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.card_view.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));

        final ItemModal itemModal = itemModalList.get(position);
        holder.item_name.setText(itemModal.getName());
        Picasso.get()
                .load(getDrawableIdFromFileName(context, itemModal.getImage()))
                .error(R.drawable.coffee)
                .resize(400, 200)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(holder.image);
        holder.consumed.setText(itemModal.getConsumed()+ " " + itemModal.getUnit() + " " + itemModal.getName() + " consumed so far");
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText edittext = new EditText(context);
                edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
                edittext.setText(String.valueOf(itemModal.getPortion_Default()));
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("How much of "+itemModal.getName()+" have you consumed (in "+itemModal.getUnit()+")");
                alert.setTitle(itemModal.getName());

                alert.setView(edittext);

                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String amount = edittext.getText().toString().trim();
                        if(amount.equals(""))
                            edittext.setError( "First name is required!" );
                        else
                            itemModal.setConsumed(itemModal.getConsumed() + Integer.parseInt(amount));
                        holder.consumed.setText(itemModal.getConsumed()+ " " + itemModal.getUnit() + " " + itemModal.getName() + " consumed so far");
                        tv.setText(String.valueOf(getTotal()));
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemModalList.size();
    }

    public static int getDrawableIdFromFileName(Context context, String nameOfDrawable) {
        return context.getResources().getIdentifier(nameOfDrawable, "drawable", context.getPackageName());
    }
}
