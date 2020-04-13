package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deleteproducts.listeners;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import java.util.List;

import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.AbstractInstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.InstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.utils.MessageUtils;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.ProductService;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.domain.ProductItem;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deleteproducts.ShareProductsCache;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.main.MainActivity;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.products.ProductsActivity;
import rx.Observable;
import rx.schedulers.Schedulers;

public class ShareProductOnClickListener implements View.OnClickListener
{
    private ProductService productService;
    private ShareProductsCache cache;

    public ShareProductOnClickListener(ShareProductsCache cache)
    {
        this.cache = cache;
        AbstractInstanceFactory instanceFactory = new InstanceFactory(cache.getActivity().getApplicationContext());
        this.productService = (ProductService) instanceFactory.createInstance(ProductService.class);
    }

    @Override
    public void onClick(View v)
    {
        MessageUtils.showAlertDialog(cache.getActivity(), R.string.delete_confirmation_title, R.string.delete_products_confirmation, deleteProducts());
    }

    private Observable<Void> deleteProducts()
    {
        Observable observable = Observable
                .defer(() -> Observable.just(deleteProductsSync()))
                .doOnError(Throwable::printStackTrace)
                .subscribeOn(Schedulers.computation());
        return observable;
    }

    private Void deleteProductsSync()
    {
        // delete products
        List<ProductItem> productList = cache.getShareProductsAdapter().getList();
        productService.deleteSelected(productList)
                .doOnError(Throwable::printStackTrace).subscribe();

        // go to products overview
        AppCompatActivity activity = cache.getActivity();
        Intent intent = new Intent(activity, ProductsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(MainActivity.LIST_ID_KEY, cache.getListId());
        activity.startActivity(intent);
        return null;
    }
}
