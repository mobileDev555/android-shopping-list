package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deleteproducts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.AbstractInstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.InstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.ProductService;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.domain.ProductItem;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.ShoppingListService;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.domain.ListItem;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deleteproducts.listeners.DeleteProductOnClickListener;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: Grebiel Jose Ifill Brito
 * Created: 21.07.16 creation date
 */
public class DeleteProductsActivity extends AppCompatActivity
{
    private ProductService productService;
    private ShoppingListService shoppingListService;
    private DeleteProductsCache cache;
    private String listId;
    private ListItem item;

    @Override
    protected final void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e){}
        setContentView(R.layout.delete_products_activity);

        AbstractInstanceFactory instanceFactory = new InstanceFactory(getApplicationContext());
        this.productService = (ProductService) instanceFactory.createInstance(ProductService.class);
        this.shoppingListService = (ShoppingListService) instanceFactory.createInstance(ShoppingListService.class);

        listId = getIntent().getStringExtra(MainActivity.LIST_ID_KEY);
        shoppingListService.getById(listId)
                .doOnNext(result -> item = result)
                .doOnCompleted(() ->
                {
                    cache = new DeleteProductsCache(this, listId, item.getListName());
                    cache.getDeleteFab().setOnClickListener(new DeleteProductOnClickListener(cache));
                    updateListView();
                })
                .doOnError(Throwable::printStackTrace)
                .subscribe();

        overridePendingTransition(0, 0);

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
        List<ProductItem> allProducts = new ArrayList<>();

        productService.getAllProducts(cache.getListId())
                .doOnNext(item -> allProducts.add(item))
                .doOnCompleted(() ->
                {
                    // sort according to last sort selection
                    final ListItem[] listItem = new ListItem[ 1 ];
                    shoppingListService.getById(listId)
                            .doOnNext(result -> listItem[ 0 ] = result)
                            .doOnCompleted(() ->
                                    {
                                        String sortBy = listItem[ 0 ].getSortCriteria();
                                        boolean sortAscending = listItem[ 0 ].isSortAscending();
                                        productService.sortProducts(allProducts, sortBy, sortAscending);

                                        cache.getDeleteProductsAdapter().setList(allProducts);
                                        cache.getDeleteProductsAdapter().notifyDataSetChanged();
                                    }
                            )
                            .doOnError(Throwable::printStackTrace)
                            .subscribe();
                })
                .doOnError(Throwable::printStackTrace)
                .subscribe();
    }
}

