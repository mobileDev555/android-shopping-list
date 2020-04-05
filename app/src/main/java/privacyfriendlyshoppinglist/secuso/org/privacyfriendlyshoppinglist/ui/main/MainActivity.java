package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.comparators.PFAComparators;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.AbstractInstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.InstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.ShoppingListService;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.domain.ListItem;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.baseactivity.BaseActivity;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deletelists.DeleteListsActivity;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deletelists.ShareListsActivity;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deleteproducts.DeleteProductsActivity;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.help.HelpActivity;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.main.listeners.AddOnClickListener;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.main.listeners.ShowDeleteListsOnClickListener;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.main.listeners.SortOnClickListener;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.main.sort.SortListsDialog;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.products.ProductsActivity;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.products.sort.SortProductsDialog;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.settings.SettingsActivity;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.settings.SettingsKeys;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.statistics.StatisticsActivity;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.tutorial.TutorialActivity;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Author: Grebiel Jose Ifill Brito
 * Created: 17.05.16 creation date
 */
public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    public static final String LIST_ID_KEY = "list.id";
    private ShoppingListService shoppingListService;
    private ShoppingListActivityCache cache;
    private Subscriber<Long> alertUpdateSubscriber;
    private Subscription alertSubscriber;

    private boolean menusVisible;


    @Override
    protected final void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AbstractInstanceFactory instanceFactory = new InstanceFactory(getApplicationContext());
        this.shoppingListService = (ShoppingListService) instanceFactory.createInstance(ShoppingListService.class);
        cache = new ShoppingListActivityCache(this);
        menusVisible = false;

//        getApplicationContext().deleteDatabase(DB.APP.getDbName());

        updateListView();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String sortBy = sharedPref.getString(SettingsKeys.LIST_SORT_BY, null);
        if ( sortBy == null )
        {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(SettingsKeys.LIST_SORT_BY, PFAComparators.SORT_BY_NAME);
            editor.putBoolean(SettingsKeys.LIST_SORT_ASCENDING, true);
            editor.commit();
        }

        cache.getNewListFab().setOnClickListener(new AddOnClickListener(cache));
        setupAlertSubscriber();

        overridePendingTransition(0, 0);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_shopping:
                        return true;
                    case R.id.navigation_statics:
                        startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
                        return true;
                    case R.id.navigation_help:
                        startActivity(new Intent(MainActivity.this, HelpActivity.class));
                        return true;
                    case R.id.navigation_settings:
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        return true;
                }
                return false;
            }
        });

        ImageView sort_img = findViewById(R.id.sort_img);
        sort_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortListsDialog sortListsDialog = SortListsDialog.newInstance(cache.getActivity());
                sortListsDialog.show(cache.getActivity().getFragmentManager(), "SortDialog");
            }
        });
        ImageView delete_img = findViewById(R.id.delete_img);
        delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = cache.getActivity();
                Intent intent = new Intent(activity, DeleteListsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        });
        ImageView share_img = findViewById(R.id.share_img);
        share_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = cache.getActivity();
                Intent intent = new Intent(activity, ShareListsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        });

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navDrawerView = (NavigationView) findViewById(R.id.nav_view);
        ImageView drawer_img = findViewById(R.id.drawer_img);
        drawer_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        updateListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.lists_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem sortItem = menu.findItem(R.id.imageview_sort);
        sortItem.setOnMenuItemClickListener(new SortOnClickListener(cache));

        MenuItem deleteItem = menu.findItem(R.id.imageview_delete);
        deleteItem.setOnMenuItemClickListener(new ShowDeleteListsOnClickListener(cache));

        sortItem.setVisible(menusVisible);
        deleteItem.setVisible(menusVisible);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected int getNavigationDrawerID()
    {
        return R.id.nav_main;
    }

    public void updateListView()
    {
        List<ListItem> allListItems = new ArrayList<>();

        shoppingListService.getAllListItems()
                .doOnNext(item -> allListItems.add(item))
                .doOnCompleted(() ->
                {
                    if ( allListItems.isEmpty() )
                    {
                        cache.getNoListsLayout().setVisibility(View.VISIBLE);
                        subscribeAlert();
                    }
                    else
                    {
                        cache.getNoListsLayout().setVisibility(View.GONE);
                        unsubscribeAlert();
                    }

                    menusVisible = !allListItems.isEmpty();
                    invalidateOptionsMenu();

                    // sort according to last sort selection
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    String sortBy = sharedPref.getString(SettingsKeys.LIST_SORT_BY, PFAComparators.SORT_BY_NAME);
                    boolean sortAscending = sharedPref.getBoolean(SettingsKeys.LIST_SORT_ASCENDING, true);
                    shoppingListService.sortList(allListItems, sortBy, sortAscending);

                    cache.getListAdapter().setList(allListItems);
                    cache.getListAdapter().notifyDataSetChanged();
                })
                .doOnError(Throwable::printStackTrace)
                .subscribe();
    }

    private void subscribeAlert()
    {
        alertSubscriber = Observable.interval(1L, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(x -> alertUpdateSubscriber.onNext(x))
                .doOnError(Throwable::printStackTrace)
                .subscribe();
    }

    private void unsubscribeAlert()
    {
        if (alertSubscriber != null && !alertSubscriber.isUnsubscribed())
        {
            cache.getAlertTextView().setVisibility(View.GONE);
            alertSubscriber.unsubscribe();
        }
    }

    private void setupAlertSubscriber()
    {
        alertUpdateSubscriber = new Subscriber<Long>()
        {
            @Override
            public void onCompleted()
            {

            }

            @Override
            public void onError(Throwable e)
            {

            }

            @Override
            public void onNext(Long time)
            {
                if ( time % 2 != 0 )
                {
                    cache.getAlertTextView().setVisibility(View.GONE);
                }
                else
                {
                    cache.getAlertTextView().setVisibility(View.VISIBLE);
                }
            }
        };
    }

    public void reorderListView(List<ListItem> sortedList)
    {
        cache.getListAdapter().setList(sortedList);
        cache.getListAdapter().notifyDataSetChanged();
    }

}
