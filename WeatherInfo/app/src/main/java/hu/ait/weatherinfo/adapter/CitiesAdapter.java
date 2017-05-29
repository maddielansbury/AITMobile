package hu.ait.weatherinfo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import hu.ait.weatherinfo.MainActivity;
import hu.ait.weatherinfo.R;
import hu.ait.weatherinfo.data.City;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvCity;

            public ViewHolder(View itemView) {
                super(itemView);
                tvCity = (TextView) itemView.findViewById(R.id.tvCity);
            }
        }

        private List<City> citiesList;
        private Context context;
        private int lastPosition = -1;

        public CitiesAdapter(List<City> citiesList, Context context) {
            this.citiesList = citiesList;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.city_row, viewGroup, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            viewHolder.tvCity.setText(citiesList.get(position).getCityName());

            setAnimation(viewHolder.itemView, position);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)context).showWeatherDetailsActivity(viewHolder.tvCity.getText().toString());
                }
            });
        }

        @Override
        public int getItemCount() {
            return citiesList.size();
        }

        public void addCity(City city) {
            citiesList.add(city);
            notifyDataSetChanged();
        }

        public void updateCity(int index, City city) {
            citiesList.set(index, city);

            notifyItemChanged(index);

        }

        public void removeCity(int index) {
            ((MainActivity)context).deleteCity(citiesList.get(index));
            citiesList.remove(index);
            notifyItemRemoved(index);
        }

        public void swapCities(int oldPosition, int newPosition) {
            if (oldPosition < newPosition) {
                for (int i = oldPosition; i < newPosition; i++) {
                    Collections.swap(citiesList, i, i + 1);
                }
            } else {
                for (int i = oldPosition; i > newPosition; i--) {
                    Collections.swap(citiesList, i, i - 1);
                }
            }
            notifyItemMoved(oldPosition, newPosition);
        }

        public City getCity(int i) {
            return citiesList.get(i);
        }

        private void setAnimation(View viewToAnimate, int position) {
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }
}
