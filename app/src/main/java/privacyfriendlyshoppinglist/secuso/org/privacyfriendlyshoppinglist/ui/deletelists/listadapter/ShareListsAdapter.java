package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deletelists.listadapter;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.ui.AbstractAdapter;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.domain.ListItem;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deletelists.DeleteListsCache;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deletelists.ShareListsCache;

public class ShareListsAdapter extends AbstractAdapter<ListItem, ShareListsCache, ShareListsItemViewHolder>
{

    public ShareListsAdapter(List<ListItem> shoppingList, ShareListsCache cache)
    {
        super(
                shoppingList,
                cache,
                R.layout.shopping_list_item);
    }

    @Override
    protected ShareListsItemViewHolder createNewViewHolder(View view)
    {
        return new ShareListsItemViewHolder(view, cache);
    }
}

