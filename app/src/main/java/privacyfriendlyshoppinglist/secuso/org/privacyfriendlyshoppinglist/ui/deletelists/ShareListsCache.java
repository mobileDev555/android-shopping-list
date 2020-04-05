package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deletelists;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.business.PFACache;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deletelists.listadapter.DeleteListsAdapter;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deletelists.listadapter.ShareListsAdapter;

public class ShareListsCache extends PFACache
{
    private AppCompatActivity activity;
//    private FloatingActionButton shareFab;
    private ShareListsAdapter shareListsAdapter;

    public ShareListsCache(ShareListsActivity activity) {
        this.activity = activity;

        shareListsAdapter = new ShareListsAdapter(new ArrayList<>(), this);

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(shareListsAdapter);

//        shareFab = (FloatingActionButton) activity.findViewById(R.id.fab_share_lists);
    }

    public AppCompatActivity getActivity()
    {
        return activity;
    }

//    public FloatingActionButton getShareFab()
//    {
//        return shareFab;
//    }

    public ShareListsAdapter getShareListsAdapter()
    {
        return shareListsAdapter;
    }
}
