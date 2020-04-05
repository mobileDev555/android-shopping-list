package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deletelists;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.comparators.PFAComparators;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.AbstractInstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.InstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.ShoppingListService;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.domain.ListItem;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deletelists.listeners.DeleteListsOnClickListener;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deletelists.listeners.ShareListsOnClickListener;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.settings.SettingsKeys;

public class ShareListsActivity extends AppCompatActivity
{
    private ShoppingListService shoppingListService;
    private ShareListsCache cache;

    @Override
    protected final void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e){}

        setContentView(R.layout.share_lists_activity);

        AbstractInstanceFactory instanceFactory = new InstanceFactory(getApplicationContext());
        this.shoppingListService = (ShoppingListService) instanceFactory.createInstance(ShoppingListService.class);
        cache = new ShareListsCache(this);

        updateListView();

        overridePendingTransition(0, 0);

//        cache.getShareFab().setOnClickListener(new ShareListsOnClickListener(cache));

        ImageView back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void updateListView()
    {
        List<ListItem> allListItems = new ArrayList<>();

        shoppingListService.getAllListItems()
                .doOnNext(item -> allListItems.add(item))
                .doOnCompleted(() ->
                {
                    // sort according to last sort selection
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    String sortBy = sharedPref.getString(SettingsKeys.LIST_SORT_BY, PFAComparators.SORT_BY_NAME);
                    boolean sortAscending = sharedPref.getBoolean(SettingsKeys.LIST_SORT_ASCENDING, true);
                    shoppingListService.sortList(allListItems, sortBy, sortAscending);

                    cache.getShareListsAdapter().setList(allListItems);
                    cache.getShareListsAdapter().notifyDataSetChanged();
                })
                .doOnError(Throwable::printStackTrace)
                .subscribe();
    }
}

