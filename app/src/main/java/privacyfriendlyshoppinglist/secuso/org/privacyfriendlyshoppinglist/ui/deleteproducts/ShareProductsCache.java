package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deleteproducts;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.business.PFACache;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deleteproducts.listadapter.ShareProductsAdapter;

public class ShareProductsCache extends PFACache
{
    private AppCompatActivity activity;
    private FloatingActionButton shareFab;
    private ShareProductsAdapter shareProductsAdapter;
    private String listId;
    private String listName;

    public ShareProductsCache(ShareProductsActivity activity, String listId, String listName)
    {
        this.activity = activity;
        this.listId = listId;
        this.listName = listName;

        shareProductsAdapter = new ShareProductsAdapter(new ArrayList<>(), this);

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(shareProductsAdapter);

//        shareFab = (FloatingActionButton) activity.findViewById(R.id.fab_share_products);
    }

    public AppCompatActivity getActivity()
    {
        return activity;
    }

//    public FloatingActionButton getShareFab()
//    {
//        return shareFab;
//    }

    public ShareProductsAdapter getShareProductsAdapter()
    {
        return shareProductsAdapter;
    }

    public String getListId()
    {
        return listId;
    }

    public String getListName()
    {
        return listName;
    }
}
