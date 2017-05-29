package hu.ait.shoppinglist.data;

import hu.ait.shoppinglist.R;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject {

    public enum ItemCategory {
        FOOD(0, R.drawable.ic_restaurant), BOOKS(1, R.drawable.ic_book), GAMES(2, R.drawable.ic_games),
        OTHER(3, R.drawable.ic_group_work);

        private int value;
        private int iconId;

        ItemCategory(int value, int iconId) {
            this.value = value;
            this.iconId = iconId;
        }

        public int getValue() {
            return value;
        }

        public int getIconId() {
            return iconId;
        }

        public static ItemCategory fromInt(int value) {
            for (ItemCategory c : ItemCategory.values()) {
                if (c.value == value) {
                    return c;
                }
            }
            return OTHER;
        }
    }

    @PrimaryKey
    private String itemID;

    private String itemTitle;
    private boolean bought;
    private boolean pinned;
    private String itemDescription;
    private int itemCategory;
    private String itemCost;


    public Item() {}

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public String getItemID() {
        return itemID;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public ItemCategory getItemCategory() {
        return ItemCategory.fromInt(itemCategory);
    }

    public void setItemCategory(int itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemCost() {
        return itemCost;
    }

    public void setItemCost(String itemCost) {
        this.itemCost = itemCost;
    }
}
