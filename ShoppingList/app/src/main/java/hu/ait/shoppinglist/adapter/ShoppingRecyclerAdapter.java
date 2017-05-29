package hu.ait.shoppinglist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import hu.ait.shoppinglist.MainActivity;
import hu.ait.shoppinglist.R;
import hu.ait.shoppinglist.data.Item;
import io.realm.Realm;

public class ShoppingRecyclerAdapter
        extends RecyclerView.Adapter<ShoppingRecyclerAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvItemTitle;
        TextView tvItemDescription;
        TextView tvItemCost;
        ImageButton btnDelete;
        ImageButton btnEdit;
        CheckBox cbBought;
        CheckBox cbPinned;

        ViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            tvItemTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvItemDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvItemCost = (TextView) itemView.findViewById(R.id.tvCost);
            btnDelete = (ImageButton) itemView.findViewById(R.id.btnDelete);
            btnEdit = (ImageButton) itemView.findViewById(R.id.btnEdit);
            cbBought = (CheckBox) itemView.findViewById(R.id.cbBought);
            cbPinned = (CheckBox) itemView.findViewById(R.id.cbPin);
        }
    }

    private List<Item> itemList;
    private Context context;
    private Realm realmShopping;

    public ShoppingRecyclerAdapter(List<Item> itemList, Context context, Realm realm) {
        this.itemList = itemList;
        this.context = context;
        this.realmShopping = realm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.shopping_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.tvItemTitle.setText(itemList.get(position).getItemTitle());
        viewHolder.ivIcon.setImageResource(
                itemList.get(position).getItemCategory().getIconId());
        viewHolder.tvItemDescription.setText(itemList.get(position).getItemDescription());
        viewHolder.tvItemCost.setText(String.format("$%s", itemList.get(position).getItemCost()));
        viewHolder.cbBought.setChecked(itemList.get(position).isBought());
        viewHolder.cbPinned.setChecked(itemList.get(position).isPinned());

        makeCheckBoxOnClickListeners(viewHolder);
        makeButtonOnClickListeners(viewHolder);
    }

    private void makeButtonOnClickListeners(final ViewHolder viewHolder) {
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(viewHolder.getAdapterPosition());//
            }
        });
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).openEditActivity(
                        itemList.get(viewHolder.getAdapterPosition()).getItemID(),
                        viewHolder.getAdapterPosition());
            }
        });
    }

    private void makeCheckBoxOnClickListeners(final ViewHolder viewHolder) {
        viewHolder.cbBought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realmShopping.beginTransaction();
                itemList.get(viewHolder.getAdapterPosition()).
                        setBought(viewHolder.cbBought.isChecked());
                realmShopping.commitTransaction();
            }
        });
        viewHolder.cbPinned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realmShopping.beginTransaction();
                itemList.get(viewHolder.getAdapterPosition()).
                        setPinned(viewHolder.cbPinned.isChecked());
                realmShopping.commitTransaction();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addItem(Item item) {
        itemList.add(item);
        notifyDataSetChanged();
    }

    public void updateItem(int index, Item item) {
        itemList.set(index, item);
        notifyItemChanged(index);
    }

    public void removeItem(int index) {
        Item item = itemList.get(index);
        realmShopping.beginTransaction();
        item.deleteFromRealm();
        realmShopping.commitTransaction();
        itemList.remove(index);
        notifyDataSetChanged();
    }

    public void clearItems() {
        realmShopping.beginTransaction();
        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);
            if (!item.isPinned()) {
                itemList.remove(i);
                item.deleteFromRealm();
            }
        }
        realmShopping.commitTransaction();
        notifyDataSetChanged();
    }

    public void swapItems(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(itemList, i, i + 1);
            }
        } else {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(itemList, i, i - 1);
            }
        }
        notifyItemMoved(oldPosition, newPosition);
    }

    public Item getItem(int i) {
        return itemList.get(i);
    }
}