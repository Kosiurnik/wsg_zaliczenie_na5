package lukaszs.idea_profit.com.wsg_zaliczenie;

/**
 * Created by LS on 03.03.2018.
 */

public class ShoppingList {
    private int ITEM_ID;
    private String ITEM_NAME;
    private String ITEM_DESCRIPTION;
    private int ITEM_USER;

    public ShoppingList(int ITEM_ID, String ITEM_NAME, String ITEM_DESCRIPTION, int ITEM_USER) {
        this.ITEM_ID = ITEM_ID;
        this.ITEM_NAME = ITEM_NAME;
        this.ITEM_DESCRIPTION = ITEM_DESCRIPTION;
        this.ITEM_USER = ITEM_USER;
    }

    public int getItemID() {
        return ITEM_ID;
    }

    public String getItemName() {
        return ITEM_NAME;
    }

    public String getItemDescription() {
        return ITEM_DESCRIPTION;
    }

    public int getItemUser() {
        return ITEM_USER;
    }
}
