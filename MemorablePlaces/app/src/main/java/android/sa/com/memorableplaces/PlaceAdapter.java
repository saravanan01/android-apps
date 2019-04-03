package android.sa.com.memorableplaces;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>{

    private List<Place> places;

    public PlaceAdapter(@NonNull List<Place> places){
        this.places = places;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        PlaceViewHolder vh = new PlaceViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        textView.setText(places.get(position).toString());
        textView.setTag(Integer.valueOf(position));

    }

    @Override
    public int getItemCount() {
        return  places.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public PlaceViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        @Override
        public void onClick(View v) {
            Integer pos = (Integer) v.getTag();
            String text = ((TextView) v).getText().toString();
            Intent intent = new Intent(v.getContext(),MapsActivity.class);
            if( ! text.equals(Constants.ADD_AN_ENTRY) ) {
                Place place = places.get(pos);
                intent.putExtra("lat",place.getLatitude());
                intent.putExtra("lng",place.getLongitude());
                intent.putExtra("name",place.getAddress());
            }
            v.getContext().startActivity(intent);

        }

        @Override
        public boolean onLongClick(View v) {
            Integer pos = (Integer) v.getTag();
            if(pos != 0) {
                MainActivity.removePlace(pos);
            }
            return true;
        }
    }
}
