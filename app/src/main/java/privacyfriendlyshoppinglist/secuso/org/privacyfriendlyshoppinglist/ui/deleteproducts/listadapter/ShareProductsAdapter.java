package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deleteproducts.listadapter;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.ui.AbstractAdapter;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.domain.ProductItem;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deleteproducts.ShareProductsCache;

public class ShareProductsAdapter  extends AbstractAdapter<ProductItem, ShareProductsCache, ShareProductsItemViewHolder>
{
    public ShareProductsAdapter(List<ProductItem> productsList, ShareProductsCache cache) {
        super(productsList, cache, R.layout.delete_product_list_item);
    }

    @Override
    protected ShareProductsItemViewHolder createNewViewHolder(View view)
    {
        return new ShareProductsItemViewHolder(view, cache);
    }
}

